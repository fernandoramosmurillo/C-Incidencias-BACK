package com.cIncidencias.api.repositorios;

import com.cIncidencias.api.ficheros.ManejadorFicheros;
import com.cIncidencias.api.modelos.Valoracion;
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
 * Repositorio para la gestión de valoraciones de los usuarios en Firestore.
 * Permite almacenar y consultar las puntuaciones y reseñas sobre la resolución de incidencias.
 */
@Repository
public class ValoracionRepository {

	private File archivoLog = new File("logs");
	private final Firestore firestore;
	private final String COLECCION = "valoraciones";

	public ValoracionRepository(Firestore firestore) {
		this.firestore = firestore;
	}

	/**
	 * Registra una nueva valoración en Firestore.
	 * @param valoracion El objeto con la puntuación y el comentario del usuario.
	 */
	public void guardarValoracion(Valoracion valoracion) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = firestore.collection(COLECCION).document(valoracion.getIdValoracion());
		ApiFuture<WriteResult> result = docRef.set(valoracion);

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		ManejadorFicheros.escribir("logs/incidencias.log", "Valoración ID: '" + valoracion.getIdValoracion() 
				+ "' registrada con éxito. Fecha: " + result.get().getUpdateTime(), false);
	}

	/**
	 * Obtiene el listado de todas las valoraciones registradas en el sistema.
	 */
	public List<Valoracion> obtenerValoraciones() throws InterruptedException, ExecutionException {
		ApiFuture<QuerySnapshot> query = firestore.collection(COLECCION).get();
		return query.get().toObjects(Valoracion.class);
	}

	/**
	 * Obtiene una valoración específica por su ID.
	 */
	public Valoracion obtenerValoracion(String idValoracion) throws InterruptedException, ExecutionException {
		DocumentReference docRef = firestore.collection(COLECCION).document(idValoracion);
		DocumentSnapshot doc = docRef.get().get();
		return doc.exists() ? doc.toObject(Valoracion.class) : null;
	}

	/**
	 * Elimina una valoración de la base de datos.
	 */
	public void eliminarValoracion(String idValoracion) throws InterruptedException, ExecutionException, IOException {
		WriteResult result = firestore.collection(COLECCION).document(idValoracion).delete().get();

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		ManejadorFicheros.escribir("logs/incidencias.log", "Valoración ID: " + idValoracion
				+ " eliminada. Fecha: " + result.getUpdateTime(), false);
	}

	/**
	 * Actualiza el contenido de una valoración existente.
	 */
	public void modificarValoracion(Valoracion valoracion) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = firestore.collection(COLECCION).document(valoracion.getIdValoracion());
		ApiFuture<WriteResult> result = docRef.set(valoracion);

		ManejadorFicheros.escribir("logs/incidencias.log", "Valoración ID: " + valoracion.getIdValoracion() 
				+ " modificada correctamente. Fecha: " + result.get().getUpdateTime(), false);
	}
}