package com.cIncidencias.api.repositorios;

import com.cIncidencias.api.ficheros.ManejadorFicheros;
import com.cIncidencias.api.modelos.Comentario;
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
 * Repositorio para la gestión de comentarios en Firestore.
 * Implementa IGenericoRepository para estandarizar las operaciones CRUD.
 */
@Repository
public class ComentarioRepository implements IGenericoRepository<Comentario> {

	private File archivoLog = new File("logs");
	private final Firestore FIRESTORE;
	private final String COLECCION = "comentarios";

	/**
	 * Inyectamos la instancia de Firestore para conectar con la base de datos de Google.
	 */
	public ComentarioRepository(Firestore firestore) {
		this.FIRESTORE = firestore;
	}

	/**
	 * Guarda un nuevo comentario en Firestore y deja huella en el log local.
	 * Si la carpeta de logs no existe, la crea sobre la marcha para evitar errores de escritura.
	 */
	@Override
	public void guardar(Comentario comentario) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(comentario.getIdComentario());
		ApiFuture<WriteResult> result = docRef.set(comentario);

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		ManejadorFicheros.escribir("logs/incidencias.log", "Comentario ID: " + comentario.getIdComentario() 
				+ " registrado con éxito. Fecha: " + result.get().getUpdateTime().toDate(), false);
	}

	/**
	 * Recupera todos los comentarios almacenados en la colección sin filtros previos.
	 */
	@Override
	public List<Comentario> obtenerTodos() throws InterruptedException, ExecutionException {
		ApiFuture<QuerySnapshot> query = FIRESTORE.collection(COLECCION).get();
		QuerySnapshot querySnapshot = query.get();
		return querySnapshot.toObjects(Comentario.class);
	}

	/**
	 * Busca un comentario por su ID único y lo convierte a un objeto Comentario si existe.
	 */
	@Override
	public Comentario obtenerPorId(String idComentario) throws InterruptedException, ExecutionException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idComentario);
		ApiFuture<DocumentSnapshot> docSnapshot = docRef.get();
		DocumentSnapshot doc = docSnapshot.get();
		return doc.exists() ? doc.toObject(Comentario.class) : null;
	}

	/**
	 * Elimina físicamente el documento de Firestore y registra la acción en el fichero de log.
	 */
	@Override
	public void eliminar(String idComentario) throws InterruptedException, ExecutionException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idComentario);
		ApiFuture<WriteResult> result = docRef.delete();
		WriteResult resultadoEscritura = result.get();

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		ManejadorFicheros.escribir("logs/incidencias.log", "Comentario ID: " + idComentario
				+ " eliminado de Firestore. Fecha: " + resultadoEscritura.getUpdateTime().toDate(), false);
	}

	/**
	 * Sobrescribe los datos de un comentario existente con la nueva información proporcionada.
	 */
	@Override
	public void modificar(Comentario comentario) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(comentario.getIdComentario());
		ApiFuture<WriteResult> result = docRef.set(comentario);
		
		Object fechaActualizacion = result.get().getUpdateTime().toDate();

		ManejadorFicheros.escribir("logs/incidencias.log", "Comentario ID: " + comentario.getIdComentario() 
				+ " modificado correctamente. Fecha: " + fechaActualizacion, false);
	}
	
	/**
	 * Actualiza solo el campo de estado de un comentario. 
	 * Si el nuevo estado es ELIMINADO, aprovecha para setear la fecha de borrado lógico.
	 */
	@Override
	public void cambiarEstado(String idComentario, ModeloBase.Estados estado)
	        throws InterruptedException, ExecutionException, IOException {

	    DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idComentario);
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
	            "Comentario ID: " + idComentario +
	                    " cambio de estado a " + estado.name() +
	                    ". Fecha: " + updateResult.getUpdateTime().toDate(),
	            false
	    );
	}
	
	/**
	 * Verifica si un comentario ya existe en la base de datos mediante su ID único.
	 * @param idComentario El identificador (PK) a comprobar.
	 * @return true si ya existe, false si está disponible.
	 */
	public boolean existePorId(String idComentario) throws InterruptedException, ExecutionException {
	    DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idComentario);
	    ApiFuture<DocumentSnapshot> query = docRef.get();
	    
	    // Si el documento existe en Firestore, retornamos true
	    return query.get().exists();
	}
}