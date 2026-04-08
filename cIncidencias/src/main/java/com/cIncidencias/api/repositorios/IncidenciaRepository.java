package com.cIncidencias.api.repositorios;

import com.cIncidencias.api.ficheros.ManejadorFicheros;
import com.cIncidencias.api.modelos.Incidencia;
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
 * Esta clase es el "puente" entre la clase Incidencia de Java y Firebase Firestore.
 * Gestiona la persistencia y consultas de la colección "incidencias".
 */
@Repository
public class IncidenciaRepository {

	private File archivoLog = new File("logs");
	private final Firestore firestore;

	// Spring inyecta automáticamente la conexión configurada en FirebaseConfig
	public IncidenciaRepository(Firestore firestore) {
		this.firestore = firestore;
	}

	/**
	 * Realiza la persistencia de un objeto Incidencia en la base de datos Firestore.
	 * @param incidencia El objeto incidencia con los datos a guardar.
	 * * @throws ExecutionException   Si ocurre un error al ejecutar la operación asíncrona.
	 * @throws InterruptedException Si se interrumpe el hilo de ejecución.
	 * @throws IOException          Si hay un error al escribir el log.
	 */
	public void guardarIncidencia(Incidencia incidencia) throws ExecutionException, InterruptedException, IOException {

		// Creamos el documento con el ID de la incidencia en la colección "incidencias"
		DocumentReference docRef = firestore.collection("incidencias").document(incidencia.getIdIncidencia());

		ApiFuture<WriteResult> result = docRef.set(incidencia);

		// Aseguramos que la carpeta de logs existe antes de escribir
		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		// Hacemos un log de la confirmación junto a la hora de actualización en Google Cloud
		ManejadorFicheros.escribir("logs/incidencias.log", "Incidencia '" + incidencia.getNombre() 
				+ "' guardada con éxito en Firestore. Fecha: " + result.get().getUpdateTime(),
				false);
	}

	/**
	 * Recupera la lista completa de documentos de la colección de incidencias.
	 * @return Una lista de objetos Incidencia.
	 * * @throws InterruptedException Si la espera de los datos es interrumpida.
	 * @throws ExecutionException   Si ocurre un error en la recuperación de los datos.
	 */
	public List<Incidencia> obtenerIncidencias() throws InterruptedException, ExecutionException {
		ApiFuture<QuerySnapshot> query = firestore.collection("incidencias").get();
		QuerySnapshot querySnapshot = query.get();
		return querySnapshot.toObjects(Incidencia.class);
	}

	/**
	 * Busca y devuelve una incidencia específica por su identificador.
	 * @param idIncidencia El identificador único del documento.
	 * * @return El objeto Incidencia encontrado.
	 * @throws InterruptedException Si se interrumpe la conexión durante la consulta.
	 * @throws ExecutionException   Si hay un fallo en la ejecución de la consulta.
	 */
	public Incidencia obtenerIncidencia(String idIncidencia) throws InterruptedException, ExecutionException {
		DocumentReference docRef = firestore.collection("incidencias").document(idIncidencia);
		ApiFuture<DocumentSnapshot> docSnapshot = docRef.get();
		return docSnapshot.get().toObject(Incidencia.class);
	}

	/**
	 * Elimina el documento de una incidencia de la base de datos y registra la acción en el log local.
	 * @param idIncidencia El ID de la incidencia que se desea borrar.
	 * * @throws InterruptedException Si la operación es interrumpida durante la espera del resultado.
	 * @throws ExecutionException   Si ocurre un error en el proceso de borrado en Firestore.
	 * @throws IOException          Si ocurre un error al escribir en el archivo de log.
	 */
	public void eliminarIncidencia(String idIncidencia) throws InterruptedException, ExecutionException, IOException {
		DocumentReference docRef = firestore.collection("incidencias").document(idIncidencia);
		ApiFuture<WriteResult> result = docRef.delete();

		// Esperamos a que la operación finalice en el servidor para obtener el resultado
		WriteResult resultadoEscritura = result.get();

		// Aseguramos que la carpeta de logs existe antes de escribir
		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		// Registramos la eliminación en el log local
		ManejadorFicheros.escribir("logs/incidencias.log", "Incidencia con ID: " + idIncidencia
				+ " eliminada con éxito de Firestore. Fecha: " + resultadoEscritura.getUpdateTime(), false);
	}

	/**
	 * Actualiza los datos de una incidencia existente en Firestore.
	 * * @param incidencia El objeto incidencia con los datos actualizados.
	 * @throws ExecutionException   Si hay un error en la tarea asíncrona.
	 * @throws InterruptedException Si se interrumpe la conexión.
	 * @throws IOException          Si falla el registro en el log local.
	 */
	public void modificarIncidencia(Incidencia incidencia) throws ExecutionException, InterruptedException, IOException {
		// Al usar set(), Firestore busca el documento por ID.
		DocumentReference docRef = firestore.collection("incidencias").document(incidencia.getIdIncidencia());

		ApiFuture<WriteResult> result = docRef.set(incidencia);

		// Esperamos el resultado para obtener la fecha de actualización de Google
		String fechaActualizacion = result.get().getUpdateTime().toString();

		ManejadorFicheros.escribir("logs/incidencias.log", "Incidencia ID: " + incidencia.getIdIncidencia() 
				+ " [" + incidencia.getNombre() + "] modificada correctamente. Fecha: " + fechaActualizacion, false);
	}
}