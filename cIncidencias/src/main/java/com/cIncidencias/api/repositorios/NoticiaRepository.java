package com.cIncidencias.api.repositorios;

import com.cIncidencias.api.ficheros.ManejadorFicheros;
import com.cIncidencias.api.modelos.Noticia;
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
 * Repositorio para la gestión de noticias y comunicados oficiales.
 * Permite informar a los usuarios sobre eventos o avisos municipales.
 */
@Repository
public class NoticiaRepository {

	private File archivoLog = new File("logs");
	private final Firestore firestore;
	private final String COLECCION = "noticias";

	public NoticiaRepository(Firestore firestore) {
		this.firestore = firestore;
	}

	/**
	 * Publica una noticia en Firestore.
	 * @param noticia El objeto con la información del comunicado.
	 */
	public void guardarNoticia(Noticia noticia) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = firestore.collection(COLECCION).document(noticia.getIdNoticia());
		ApiFuture<WriteResult> result = docRef.set(noticia);

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		ManejadorFicheros.escribir("logs/incidencias.log", "Noticia '" + noticia.getTitulo() 
				+ "' publicada con éxito. Fecha: " + result.get().getUpdateTime(), false);
	}

	/**
	 * Obtiene el listado de todas las noticias (ideal para el feed principal).
	 */
	public List<Noticia> obtenerNoticias() throws InterruptedException, ExecutionException {
		ApiFuture<QuerySnapshot> query = firestore.collection(COLECCION).get();
		return query.get().toObjects(Noticia.class);
	}

	/**
	 * Obtiene una noticia específica por su ID.
	 */
	public Noticia obtenerNoticia(String idNoticia) throws InterruptedException, ExecutionException {
		DocumentReference docRef = firestore.collection(COLECCION).document(idNoticia);
		DocumentSnapshot doc = docRef.get().get();
		return doc.exists() ? doc.toObject(Noticia.class) : null;
	}

	/**
	 * Elimina una noticia de la base de datos.
	 */
	public void eliminarNoticia(String idNoticia) throws InterruptedException, ExecutionException, IOException {
		WriteResult result = firestore.collection(COLECCION).document(idNoticia).delete().get();

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		ManejadorFicheros.escribir("logs/incidencias.log", "Noticia ID: " + idNoticia
				+ " eliminada. Fecha: " + result.getUpdateTime(), false);
	}

	/**
	 * Actualiza el contenido de una noticia existente.
	 */
	public void modificarNoticia(Noticia noticia) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = firestore.collection(COLECCION).document(noticia.getIdNoticia());
		ApiFuture<WriteResult> result = docRef.set(noticia);

		ManejadorFicheros.escribir("logs/incidencias.log", "Noticia ID: " + noticia.getIdNoticia() 
				+ " modificada correctamente. Fecha: " + result.get().getUpdateTime(), false);
	}
}