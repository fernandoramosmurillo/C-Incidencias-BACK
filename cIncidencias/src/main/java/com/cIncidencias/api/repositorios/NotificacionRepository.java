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
 * Proporciona métodos para el envío, consulta y borrado de avisos del sistema.
 */
@Repository
public class NotificacionRepository {

	private File archivoLog = new File("logs");
	private final Firestore firestore;
	private final String COLECCION = "notificaciones";

	public NotificacionRepository(Firestore firestore) {
		this.firestore = firestore;
	}

	/**
	 * Registra una nueva notificación en Firestore.
	 * @param notificacion El objeto con la información de la alerta.
	 */
	public void guardarNotificacion(Notificacion notificacion) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = firestore.collection(COLECCION).document(notificacion.getIdNotificacion());
		ApiFuture<WriteResult> result = docRef.set(notificacion);

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		ManejadorFicheros.escribir("logs/incidencias.log", "Notificación ID: '" + notificacion.getIdNotificacion() 
				+ "' enviada con éxito. Fecha: " + result.get().getUpdateTime(), false);
	}

	/**
	 * Obtiene el listado de todas las notificaciones del sistema.
	 */
	public List<Notificacion> obtenerNotificaciones() throws InterruptedException, ExecutionException {
		ApiFuture<QuerySnapshot> query = firestore.collection(COLECCION).get();
		return query.get().toObjects(Notificacion.class);
	}

	/**
	 * Obtiene una notificación específica por su ID.
	 */
	public Notificacion obtenerNotificacion(String idNotificacion) throws InterruptedException, ExecutionException {
		DocumentReference docRef = firestore.collection(COLECCION).document(idNotificacion);
		DocumentSnapshot doc = docRef.get().get();
		return doc.exists() ? doc.toObject(Notificacion.class) : null;
	}

	/**
	 * Elimina una notificación de la base de datos.
	 */
	public void eliminarNotificacion(String idNotificacion) throws InterruptedException, ExecutionException, IOException {
		WriteResult result = firestore.collection(COLECCION).document(idNotificacion).delete().get();

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		ManejadorFicheros.escribir("logs/incidencias.log", "Notificación ID: " + idNotificacion
				+ " eliminada. Fecha: " + result.getUpdateTime(), false);
	}

	/**
	 * Actualiza el contenido de una notificación existente.
	 */
	public void modificarNotificacion(Notificacion notificacion) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = firestore.collection(COLECCION).document(notificacion.getIdNotificacion());
		ApiFuture<WriteResult> result = docRef.set(notificacion);

		ManejadorFicheros.escribir("logs/incidencias.log", "Notificación ID: " + notificacion.getIdNotificacion() 
				+ " modificada correctamente. Fecha: " + result.get().getUpdateTime(), false);
	}
}