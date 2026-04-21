package com.cIncidencias.api.repositorios;

import com.cIncidencias.api.ficheros.ManejadorFicheros;
import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.modelos.ModeloBase.Estados;
import com.cIncidencias.api.modelos.Notificacion;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
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
 * Se asegura de que cada alerta (ya sea por una incidencia o un aviso del sistema) quede persistida.
 */
@Repository
public class NotificacionRepository implements IGenericoRepository<Notificacion> {

	private File archivoLog = new File("logs");
	private final Firestore FIRESTORE;
	private final String COLECCION = "notificaciones";

	public NotificacionRepository(Firestore firestore) {
		this.FIRESTORE = firestore;
	}

	/**
	 * Guarda una nueva notificación en la base de datos.
	 * Al igual que en el resto de repositorios, registramos el envío en el log 
	 * local para tener un respaldo de qué alertas se han disparado.
	 */
	@Override
	public void guardar(Notificacion notificacion) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(notificacion.getIdNotificacion());
		ApiFuture<WriteResult> result = docRef.set(notificacion);

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		// Aplicado .toDate() para mostrar la fecha local en el log
		ManejadorFicheros.escribir("logs/incidencias.log", "Notificación ID: '" + notificacion.getIdNotificacion() 
				+ "' enviada con éxito. Fecha: " + result.get().getUpdateTime().toDate(), false);
	}

	/**
	 * Recupera el historial completo de notificaciones del sistema.
	 */
	@Override
	public List<Notificacion> obtenerTodos() throws InterruptedException, ExecutionException {
		ApiFuture<QuerySnapshot> query = FIRESTORE.collection(COLECCION).get();
		QuerySnapshot querySnapshot = query.get();
		return querySnapshot.toObjects(Notificacion.class);
	}

	/**
	 * Busca una notificación específica por su ID.
	 */
	@Override
	public Notificacion obtenerPorId(String idNotificacion) throws InterruptedException, ExecutionException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idNotificacion);
		ApiFuture<DocumentSnapshot> docSnapshot = docRef.get();
		DocumentSnapshot doc = docSnapshot.get();
		return doc.exists() ? doc.toObject(Notificacion.class) : null;
	}

	/**
	 * Elimina permanentemente una notificación de Firestore.
	 */
	@Override
	public void eliminar(String idNotificacion) throws InterruptedException, ExecutionException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idNotificacion);
		ApiFuture<WriteResult> result = docRef.delete();
		WriteResult resultadoEscritura = result.get();

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		// Aplicado .toDate() para mostrar la fecha local en el log
		ManejadorFicheros.escribir("logs/incidencias.log", "Notificación ID: " + idNotificacion
				+ " eliminada de Firestore. Fecha: " + resultadoEscritura.getUpdateTime().toDate(), false);
	}

	/**
	 * Permite modificar los datos de una notificación enviada.
	 */
	@Override
	public void modificar(Notificacion notificacion) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(notificacion.getIdNotificacion());
		ApiFuture<WriteResult> result = docRef.set(notificacion);
		
		// Aplicado .toDate() para mantener la consistencia local
		Object fechaActualizacion = result.get().getUpdateTime().toDate();

		ManejadorFicheros.escribir("logs/incidencias.log", "Notificación ID: " + notificacion.getIdNotificacion() 
				+ " modificada correctamente. Fecha: " + fechaActualizacion, false);
	}
	
	/**
	 * Cambia el estado de la notificación (por ejemplo, de ACTIVO a INACTIVO).
	 * Si el estado es ELIMINADO, se realiza el borrado lógico registrando la fecha exacta.
	 */
	@Override
	public void cambiarEstado(String idNotificacion, ModeloBase.Estados estado)
	        throws InterruptedException, ExecutionException, IOException {

	    DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idNotificacion);
	    WriteResult updateResult = null;
	    
	    if (!estado.equals(Estados.ELIMINADO)) {
	        // Cambio de estado normal para la notificación
	        ApiFuture<WriteResult> result = docRef.update("estado", estado.name());
		    updateResult = result.get();
	    } else {
	        // Si eliminamos la notificación, guardamos el estado y la fecha de borrado a la vez
	        ApiFuture<WriteResult> result = docRef.update(
	    			"estado", estado.name(),
	    			"fechaEliminacion", Timestamp.now()
	    	);
		    updateResult = result.get();
	    }

	    if (!archivoLog.exists()) {
	        archivoLog.mkdirs();
	    }

	    ManejadorFicheros.escribir(
	            "logs/incidencias.log",
	            "Notificación ID: " + idNotificacion +
	                    " cambio de estado a " + estado.name() +
	                    ". Fecha: " + updateResult.getUpdateTime().toDate(),
	            false
	    );
	}
}