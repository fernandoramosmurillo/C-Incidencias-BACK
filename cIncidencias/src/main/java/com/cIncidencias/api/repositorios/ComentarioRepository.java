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

	public ComentarioRepository(Firestore firestore) {
		this.FIRESTORE = firestore;
	}

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

	@Override
	public List<Comentario> obtenerTodos() throws InterruptedException, ExecutionException {
		ApiFuture<QuerySnapshot> query = FIRESTORE.collection(COLECCION).get();
		QuerySnapshot querySnapshot = query.get();
		return querySnapshot.toObjects(Comentario.class);
	}

	@Override
	public Comentario obtenerPorId(String idComentario) throws InterruptedException, ExecutionException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idComentario);
		ApiFuture<DocumentSnapshot> docSnapshot = docRef.get();
		DocumentSnapshot doc = docSnapshot.get();
		return doc.exists() ? doc.toObject(Comentario.class) : null;
	}

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

	@Override
	public void modificar(Comentario comentario) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(comentario.getIdComentario());
		ApiFuture<WriteResult> result = docRef.set(comentario);
		
		// Cambiado a .toDate() para consistencia local
		Object fechaActualizacion = result.get().getUpdateTime().toDate();

		ManejadorFicheros.escribir("logs/incidencias.log", "Comentario ID: " + comentario.getIdComentario() 
				+ " modificado correctamente. Fecha: " + fechaActualizacion, false);
	}
	
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
}