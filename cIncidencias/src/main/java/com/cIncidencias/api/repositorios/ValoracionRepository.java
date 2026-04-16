package com.cIncidencias.api.repositorios;

import com.cIncidencias.api.ficheros.ManejadorFicheros;
import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.modelos.ModeloBase.Estados;
import com.cIncidencias.api.modelos.Valoracion;
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
 * Repositorio para la gestión de valoraciones de los usuarios en Firestore.
 * Implementa IGenericoRepository para estandarizar el feedback sobre la resolución de incidencias.
 */
@Repository
public class ValoracionRepository implements IGenericoRepository<Valoracion> {

	private File archivoLog = new File("logs");
	private final Firestore FIRESTORE;
	private final String COLECCION = "valoraciones";

	public ValoracionRepository(Firestore firestore) {
		this.FIRESTORE = firestore;
	}

	@Override
	public void guardar(Valoracion valoracion) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(valoracion.getIdValoracion());
		ApiFuture<WriteResult> result = docRef.set(valoracion);

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		// Aplicado .toDate() para mostrar la fecha local en el log
		ManejadorFicheros.escribir("logs/incidencias.log", "Valoración ID: '" + valoracion.getIdValoracion() 
				+ "' registrada con éxito. Fecha: " + result.get().getUpdateTime().toDate(), false);
	}

	@Override
	public List<Valoracion> obtenerTodos() throws InterruptedException, ExecutionException {
		ApiFuture<QuerySnapshot> query = FIRESTORE.collection(COLECCION).get();
		QuerySnapshot querySnapshot = query.get();
		return querySnapshot.toObjects(Valoracion.class);
	}

	@Override
	public Valoracion obtenerPorId(String idValoracion) throws InterruptedException, ExecutionException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idValoracion);
		ApiFuture<DocumentSnapshot> docSnapshot = docRef.get();
		DocumentSnapshot doc = docSnapshot.get();
		return doc.exists() ? doc.toObject(Valoracion.class) : null;
	}

	@Override
	public void eliminar(String idValoracion) throws InterruptedException, ExecutionException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idValoracion);
		ApiFuture<WriteResult> result = docRef.delete();
		WriteResult resultadoEscritura = result.get();

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		// Aplicado .toDate() para mostrar la fecha local en el log
		ManejadorFicheros.escribir("logs/incidencias.log", "Valoración ID: " + idValoracion
				+ " eliminada de Firestore. Fecha: " + resultadoEscritura.getUpdateTime().toDate(), false);
	}

	@Override
	public void modificar(Valoracion valoracion) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(valoracion.getIdValoracion());
		ApiFuture<WriteResult> result = docRef.set(valoracion);
		
		// Aplicado .toDate() para consistencia local
		Object fechaActualizacion = result.get().getUpdateTime().toDate();

		ManejadorFicheros.escribir("logs/incidencias.log", "Valoración ID: " + valoracion.getIdValoracion() 
				+ " modificada correctamente. Fecha: " + fechaActualizacion, false);
	}
	
	@Override
	public void cambiarEstado(String idValoracion, ModeloBase.Estados estado)
	        throws InterruptedException, ExecutionException, IOException {

	    DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idValoracion);
	    WriteResult updateResult = null;
	    
	    if (!estado.equals(Estados.ELIMINADO)) {
	        // Cambio de estado normal para la valoración
	        ApiFuture<WriteResult> result = docRef.update("estado", estado.name());
		    updateResult = result.get();
	    } else {
	        // Si la valoración se elimina, guardamos el estado y la fecha de borrado en Firestore
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
	            "Valoración ID: " + idValoracion +
	                    " cambio de estado a " + estado.name() +
	                    ". Fecha: " + updateResult.getUpdateTime().toDate(),
	            false
	    );
	}
}