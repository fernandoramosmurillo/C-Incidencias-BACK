package com.cIncidencias.api.repositorios;

import com.cIncidencias.api.ficheros.ManejadorFicheros;
import com.cIncidencias.api.modelos.Incidencia;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Repositorio para la gestión de incidencias ciudadanas en Firestore.
 * Implementa IGenericoRepository para asegurar la consistencia en el acceso a datos.
 */
@Repository
public class IncidenciaRepository implements IGenericoRepository<Incidencia> {

	private File archivoLog = new File("logs");
	private final Firestore FIRESTORE;
	private final String COLECCION = "incidencias";

	public IncidenciaRepository(Firestore firestore) {
		this.FIRESTORE = firestore;
	}

	@Override
	public void guardar(Incidencia incidencia) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(incidencia.getIdIncidencia());
		ApiFuture<WriteResult> result = docRef.set(incidencia);

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		ManejadorFicheros.escribir("logs/incidencias.log", "Incidencia ID: " + incidencia.getIdIncidencia() 
				+ " registrada con éxito. Fecha: " + result.get().getUpdateTime().toDate(), false);
	}

	@Override
	public List<Incidencia> obtenerTodos() throws InterruptedException, ExecutionException {
		ApiFuture<QuerySnapshot> query = FIRESTORE.collection(COLECCION).get();
		QuerySnapshot querySnapshot = query.get();
		return querySnapshot.toObjects(Incidencia.class);
	}

	@Override
	public Incidencia obtenerPorId(String idIncidencia) throws InterruptedException, ExecutionException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idIncidencia);
		ApiFuture<DocumentSnapshot> docSnapshot = docRef.get();
		DocumentSnapshot doc = docSnapshot.get();
		return doc.exists() ? doc.toObject(Incidencia.class) : null;
	}

	@Override
	public void eliminar(String idIncidencia) throws InterruptedException, ExecutionException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idIncidencia);
		ApiFuture<WriteResult> result = docRef.delete();
		WriteResult resultadoEscritura = result.get();

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		ManejadorFicheros.escribir("logs/incidencias.log", "Incidencia ID: " + idIncidencia
				+ " eliminada de Firestore. Fecha: " + resultadoEscritura.getUpdateTime().toDate(), false);
	}

	@Override
	public void modificar(Incidencia incidencia) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(incidencia.getIdIncidencia());
		ApiFuture<WriteResult> result = docRef.set(incidencia);
		
		// Aplicado .toDate() para mantener la consistencia en los logs
		Object fechaActualizacion = result.get().getUpdateTime().toDate();

		ManejadorFicheros.escribir("logs/incidencias.log", "Incidencia ID: " + incidencia.getIdIncidencia() 
				+ " modificada correctamente. Fecha: " + fechaActualizacion, false);
	}
}