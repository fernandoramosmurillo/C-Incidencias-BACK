package com.cIncidencias.api.repositorios;

import com.cIncidencias.api.ficheros.ManejadorFicheros;
import com.cIncidencias.api.modelos.Incidencia;
import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.modelos.ModeloBase.Estados;
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
 * Repositorio para la gestión de incidencias ciudadanas en Firestore.
 * Implementa IGenericoRepository para asegurar la consistencia en el acceso a datos.
 * Centraliza toda la lógica de persistencia para los reportes de averías y desperfectos.
 */
@Repository
public class IncidenciaRepository implements IGenericoRepository<Incidencia> {

	private File archivoLog = new File("logs");
	private final Firestore FIRESTORE;
	private final String COLECCION = "incidencias";

	public IncidenciaRepository(Firestore firestore) {
		this.FIRESTORE = firestore;
	}

	/**
	 * Registra una incidencia en Firestore. 
	 * Si es la primera vez que se usa el sistema, crea la carpeta de logs 
	 * para guardar el histórico de operaciones.
	 */
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

	/**
	 * Obtiene el listado completo de incidencias reportadas.
	 */
	@Override
	public List<Incidencia> obtenerTodos() throws InterruptedException, ExecutionException {
		ApiFuture<QuerySnapshot> query = FIRESTORE.collection(COLECCION).get();
		QuerySnapshot querySnapshot = query.get();
		return querySnapshot.toObjects(Incidencia.class);
	}

	/**
	 * Busca una incidencia por su identificador único para consultar su estado o detalles.
	 */
	@Override
	public Incidencia obtenerPorId(String idIncidencia) throws InterruptedException, ExecutionException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idIncidencia);
		ApiFuture<DocumentSnapshot> docSnapshot = docRef.get();
		DocumentSnapshot doc = docSnapshot.get();
		return doc.exists() ? doc.toObject(Incidencia.class) : null;
	}

	/**
	 * Elimina el documento de la incidencia de forma definitiva en Firestore.
	 */
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

	/**
	 * Actualiza la información de una incidencia (cambios en descripción, fotos, etc).
	 */
	@Override
	public void modificar(Incidencia incidencia) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(incidencia.getIdIncidencia());
		ApiFuture<WriteResult> result = docRef.set(incidencia);
		
		Object fechaActualizacion = result.get().getUpdateTime().toDate();

		ManejadorFicheros.escribir("logs/incidencias.log", "Incidencia ID: " + incidencia.getIdIncidencia() 
				+ " modificada correctamente. Fecha: " + fechaActualizacion, false);
	}
	
	/**
	 * Gestiona el ciclo de vida de la incidencia (Activa, Bloqueada o Eliminada lógicamente).
	 * Si el estado es ELIMINADO, se registra automáticamente la fecha actual como fecha de baja.
	 */
	@Override
	public void cambiarEstado(String idIncidencia, ModeloBase.Estados estado)
	        throws InterruptedException, ExecutionException, IOException {

	    DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idIncidencia);
	    WriteResult updateResult = null;
	    
	    if (!estado.equals(Estados.ELIMINADO)) {
	    	ApiFuture<WriteResult> result = docRef.update("estado", estado.name());
		    updateResult = result.get();
	    } else {
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
	            "Incidencia ID: " + idIncidencia +
	                    " cambio de estado a " + estado.name() +
	                    ". Fecha: " + updateResult.getUpdateTime().toDate(),
	            false
	    );
	}
	
	/**
	 * Verifica si una incidencia ya existe en la base de datos mediante su ID único.
	 * @param idIncidencia El identificador a comprobar.
	 * @return true si ya existe, false si está disponible.
	 */
	public boolean existePorId(String idIncidencia) throws InterruptedException, ExecutionException {
	    DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idIncidencia);
	    ApiFuture<DocumentSnapshot> query = docRef.get();
	    
	    // Si el documento existe en Firestore, retornamos true
	    return query.get().exists();
	}
}