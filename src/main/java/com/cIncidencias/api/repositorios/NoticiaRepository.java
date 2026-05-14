package com.cIncidencias.api.repositorios;

import com.cIncidencias.api.ficheros.ManejadorFicheros;
import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.modelos.ModeloBase.Estados;
import com.cIncidencias.api.modelos.Noticia;
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
 * Repositorio para la gestión de noticias y comunicados oficiales en Firestore.
 * Implementa IGenericoRepository para estandarizar el acceso a la información municipal.
 * Se encarga de que cada bando o noticia llegue correctamente a la base de datos de Google.
 */
@Repository
public class NoticiaRepository implements IGenericoRepository<Noticia> {

	private File archivoLog = new File("logs");
	private final Firestore FIRESTORE;
	private final String COLECCION = "noticias";

	public NoticiaRepository(Firestore firestore) {
		this.FIRESTORE = firestore;
	}

	/**
	 * Publica una noticia nueva en Firestore. 
	 * Además de guardarla, genera un registro en el log local con el título 
	 * de la noticia para que sea más fácil de identificar en las auditorías.
	 */
	@Override
	public void guardar(Noticia noticia) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(noticia.getIdNoticia());
		ApiFuture<WriteResult> result = docRef.set(noticia);

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		// Aplicado .toDate() para mostrar la fecha local en el log
		ManejadorFicheros.escribir("logs/incidencias.log", "Noticia '" + noticia.getTitulo() 
				+ "' publicada con éxito. Fecha: " + result.get().getUpdateTime().toDate(), false);
	}

	/**
	 * Recupera todas las noticias del tablón municipal.
	 */
	@Override
	public List<Noticia> obtenerTodos() throws InterruptedException, ExecutionException {
		ApiFuture<QuerySnapshot> query = FIRESTORE.collection(COLECCION).get();
		QuerySnapshot querySnapshot = query.get();
		return querySnapshot.toObjects(Noticia.class);
	}

	/**
	 * Obtiene el contenido completo de una noticia mediante su ID único.
	 */
	@Override
	public Noticia obtenerPorId(String idNoticia) throws InterruptedException, ExecutionException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idNoticia);
		ApiFuture<DocumentSnapshot> docSnapshot = docRef.get();
		DocumentSnapshot doc = docSnapshot.get();
		return doc.exists() ? doc.toObject(Noticia.class) : null;
	}

	/**
	 * Borra físicamente la noticia de Firestore.
	 */
	@Override
	public void eliminar(String idNoticia) throws InterruptedException, ExecutionException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idNoticia);
		ApiFuture<WriteResult> result = docRef.delete();
		WriteResult resultadoEscritura = result.get();

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		// Aplicado .toDate() para mostrar la fecha local en el log
		ManejadorFicheros.escribir("logs/incidencias.log", "Noticia ID: " + idNoticia
				+ " eliminada. Fecha: " + resultadoEscritura.getUpdateTime().toDate(), false);
	}

	/**
	 * Modifica una noticia ya existente (útil para corregir erratas o actualizar información).
	 */
	@Override
	public void modificar(Noticia noticia) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(noticia.getIdNoticia());
		ApiFuture<WriteResult> result = docRef.set(noticia);
		
		// Aplicado .toDate() para consistencia local
		Object fechaActualizacion = result.get().getUpdateTime().toDate();

		ManejadorFicheros.escribir("logs/incidencias.log", "Noticia ID: " + noticia.getIdNoticia() 
				+ " modificada correctamente. Fecha: " + fechaActualizacion, false);
	}
	
	/**
	 * Actualiza el estado de la noticia (por ejemplo, para despublicarla pasando a INACTIVO).
	 * Si el estado es ELIMINADO, se encarga de dejar constancia de la fecha de borrado lógico.
	 */
	@Override
	public void cambiarEstado(String idNoticia, ModeloBase.Estados estado)
	        throws InterruptedException, ExecutionException, IOException {

	    DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idNoticia);
	    WriteResult updateResult = null;
	    
	    if (!estado.equals(Estados.ELIMINADO)) {
	    	// Cambio de estado estándar para la noticia
	    	ApiFuture<WriteResult> result = docRef.update("estado", estado.name());
		    updateResult = result.get();
	    } else {
	    	// Si eliminamos la noticia, registramos también la fecha del borrado
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
	            "Noticia ID: " + idNoticia +
	                    " cambio de estado a " + estado.name() +
	                    ". Fecha: " + updateResult.getUpdateTime().toDate(),
	            false
	    );
	}
	
	/**
	 * Verifica si una noticia ya existe en Firestore utilizando su identificador.
	 * Mantiene la coherencia técnica con el resto de repositorios del sistema.
	 * @param idNoticia El identificador único de la noticia.
	 * @return true si la noticia existe, false en caso contrario.
	 */
	public boolean existePorId(String idNoticia) throws InterruptedException, ExecutionException {
	    DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idNoticia);
	    
	    ApiFuture<DocumentSnapshot> query = docRef.get();

	    // Si el documento existe en la colección de noticias, retornamos true
	    return query.get().exists();
	}
}