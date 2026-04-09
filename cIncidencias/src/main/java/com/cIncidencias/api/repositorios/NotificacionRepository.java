package com.cIncidencias.api.repositorios;

import com.cIncidencias.api.ficheros.ManejadorFicheros;
import com.cIncidencias.api.modelos.Notificacion;
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
 * Repositorio para la gestión de notificaciones y alertas al usuario en Firestore.
 * Implementa IGenericoRepository para estandarizar el flujo de avisos del sistema.
 */
@Repository
public class NotificacionRepository implements IGenericoRepository<Notificacion> {

	private File archivoLog = new File("logs");
	private final Firestore FIRESTORE;
	private final String COLECCION = "notificaciones";

	public NotificacionRepository(Firestore firestore) {
		this.FIRESTORE = firestore;
	}

	@Override
	public void guardar(Notificacion notificacion) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(notificacion.getIdNotificacion());
		ApiFuture<WriteResult> result = docRef.set(notificacion);

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		ManejadorFicheros.escribir("logs/incidencias.log", "Notificación ID: '" + notificacion.getIdNotificacion() 
				+ "' enviada con éxito. Fecha: " + result.get().getUpdateTime(), false);
	}

	@Override
	public List<Notificacion> obtenerTodos() throws InterruptedException, ExecutionException {
		ApiFuture<QuerySnapshot> query = FIRESTORE.collection(COLECCION).get();
		QuerySnapshot querySnapshot = query.get();
		return querySnapshot.toObjects(Notificacion.class);
	}

	@Override
	public Notificacion obtenerPorId(String idNotificacion) throws InterruptedException, ExecutionException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idNotificacion);
		ApiFuture<DocumentSnapshot> docSnapshot = docRef.get();
		DocumentSnapshot doc = docSnapshot.get();
		return doc.exists() ? doc.toObject(Notificacion.class) : null;
	}

	@Override
	public void eliminar(String idNotificacion) throws InterruptedException, ExecutionException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idNotificacion);
		ApiFuture<WriteResult> result = docRef.delete();
		WriteResult resultadoEscritura = result.get();

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		ManejadorFicheros.escribir("logs/incidencias.log", "Notificación ID: " + idNotificacion
				+ " eliminada de Firestore. Fecha: " + resultadoEscritura.getUpdateTime(), false);
	}

	@Override
	public void modificar(Notificacion notificacion) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(notificacion.getIdNotificacion());
		ApiFuture<WriteResult> result = docRef.set(notificacion);
		String fechaActualizacion = result.get().getUpdateTime().toString();

		ManejadorFicheros.escribir("logs/incidencias.log", "Notificación ID: " + notificacion.getIdNotificacion() 
				+ " modificada correctamente. Fecha: " + fechaActualizacion, false);
	}
}