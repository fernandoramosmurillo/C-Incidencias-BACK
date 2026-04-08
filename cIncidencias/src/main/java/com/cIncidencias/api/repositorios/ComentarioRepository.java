package com.cIncidencias.api.repositorios;

import com.cIncidencias.api.ficheros.ManejadorFicheros;
import com.cIncidencias.api.modelos.Comentario;
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
 * Repositorio para la gestión de comentarios en Firestore.
 * Al ser independiente, permite buscar comentarios por usuario o por incidencia de forma eficiente.
 */
@Repository
public class ComentarioRepository {

	private File archivoLog = new File("logs");
	private final Firestore firestore;

	public ComentarioRepository(Firestore firestore) {
		this.firestore = firestore;
	}

	/**
	 * Guarda un comentario en la colección raíz "comentarios".
	 * El objeto Comentario debe incluir el idUsuario y el idIncidencia para mantener la relación.
	 */
	public void guardarComentario(Comentario comentario) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = firestore.collection("comentarios").document(comentario.getIdComentario());
		ApiFuture<WriteResult> result = docRef.set(comentario);

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		ManejadorFicheros.escribir("logs/incidencias.log", "Comentario ID: " + comentario.getIdComentario() 
				+ " registrado con éxito. Fecha: " + result.get().getUpdateTime(), false);
	}

	/**
	 * Recupera todos los comentarios de la base de datos.
	 */
	public List<Comentario> obtenerComentarios() throws InterruptedException, ExecutionException {
		ApiFuture<QuerySnapshot> query = firestore.collection("comentarios").get();
		QuerySnapshot querySnapshot = query.get();
		return querySnapshot.toObjects(Comentario.class);
	}

	/**
	 * Busca un comentario específico por su identificador único.
	 */
	public Comentario obtenerComentario(String idComentario) throws InterruptedException, ExecutionException {
		DocumentReference docRef = firestore.collection("comentarios").document(idComentario);
		ApiFuture<DocumentSnapshot> docSnapshot = docRef.get();
		return docSnapshot.get().toObject(Comentario.class);
	}

	/**
	 * Elimina un comentario de Firestore.
	 */
	public void eliminarComentario(String idComentario) throws InterruptedException, ExecutionException, IOException {
		DocumentReference docRef = firestore.collection("comentarios").document(idComentario);
		ApiFuture<WriteResult> result = docRef.delete();
		WriteResult resultadoEscritura = result.get();

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		ManejadorFicheros.escribir("logs/incidencias.log", "Comentario ID: " + idComentario
				+ " eliminado de Firestore. Fecha: " + resultadoEscritura.getUpdateTime(), false);
	}

	/**
	 * Actualiza un comentario existente (por ejemplo, para edición o moderación).
	 */
	public void modificarComentario(Comentario comentario) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = firestore.collection("comentarios").document(comentario.getIdComentario());
		ApiFuture<WriteResult> result = docRef.set(comentario);
		String fechaActualizacion = result.get().getUpdateTime().toString();

		ManejadorFicheros.escribir("logs/incidencias.log", "Comentario ID: " + comentario.getIdComentario() 
				+ " modificado correctamente. Fecha: " + fechaActualizacion, false);
	}
}