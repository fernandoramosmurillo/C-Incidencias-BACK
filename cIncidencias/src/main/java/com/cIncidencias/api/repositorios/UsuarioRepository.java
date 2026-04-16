package com.cIncidencias.api.repositorios;

import com.cIncidencias.api.ficheros.ManejadorFicheros;
import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.modelos.ModeloBase.Estados;
import com.cIncidencias.api.modelos.Usuario;
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
 * Repositorio para la gestión de usuarios en Firestore. Implementa
 * IGenericoRepository para estandarizar la persistencia de ciudadanos y admins.
 */
@Repository
public class UsuarioRepository implements IGenericoRepository<Usuario> {

	private File archivoLog = new File("logs");
	private final Firestore FIRESTORE;
	private final String COLECCION = "usuarios";

	public UsuarioRepository(Firestore firestore) {
		this.FIRESTORE = firestore;
	}

	@Override
	public void guardar(Usuario usuario) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(usuario.getIdUsuario());
		ApiFuture<WriteResult> result = docRef.set(usuario);

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		// Añadido .toDate() para mostrar la hora local de España
		ManejadorFicheros
				.escribir("logs/incidencias.log",
						"Usuario " + usuario.getNombre() + " " + usuario.getApellidos()
								+ " guardado con éxito en Firestore. Fecha: " + result.get().getUpdateTime().toDate(),
						false);
	}

	@Override
	public List<Usuario> obtenerTodos() throws InterruptedException, ExecutionException {
		ApiFuture<QuerySnapshot> query = FIRESTORE.collection(COLECCION).get();
		QuerySnapshot querySnapshot = query.get();
		return querySnapshot.toObjects(Usuario.class);
	}

	@Override
	public Usuario obtenerPorId(String idUsuario) throws InterruptedException, ExecutionException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idUsuario);
		ApiFuture<DocumentSnapshot> docSnapshot = docRef.get();
		DocumentSnapshot doc = docSnapshot.get();
		return doc.exists() ? doc.toObject(Usuario.class) : null;
	}

	@Override
	public void eliminar(String idUsuario) throws InterruptedException, ExecutionException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idUsuario);
		ApiFuture<WriteResult> result = docRef.delete();
		WriteResult resultadoEscritura = result.get();

		if (!archivoLog.exists()) {
			archivoLog.mkdirs();
		}

		// Añadido .toDate() para mostrar la hora local de España
		ManejadorFicheros.escribir("logs/incidencias.log", "Usuario con ID: " + idUsuario
				+ " eliminado con éxito de Firestore. Fecha: " + resultadoEscritura.getUpdateTime().toDate(), false);
	}

	@Override
	public void modificar(Usuario usuario) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(usuario.getIdUsuario());
		ApiFuture<WriteResult> result = docRef.set(usuario);

		// Cambiado a .toDate() para consistencia local
		Object fechaActualizacion = result.get().getUpdateTime().toDate();

		ManejadorFicheros.escribir("logs/incidencias.log", "Usuario ID: " + usuario.getIdUsuario() + " ["
				+ usuario.getNombre() + "] modificado correctamente. Fecha: " + fechaActualizacion, false);
	}

	@Override
	public void cambiarEstado(String idUsuario, ModeloBase.Estados estado)
	        throws InterruptedException, ExecutionException, IOException {

	    DocumentReference docRef = FIRESTORE.collection(COLECCION).document(idUsuario);
	    WriteResult updateResult = null;
	    
	    if (!estado.equals(Estados.ELIMINADO)) {
	        // Cambio de estado normal del usuario (ej: de ACTIVO a INACTIVO)
	        ApiFuture<WriteResult> result = docRef.update("estado", estado.name());
		    updateResult = result.get();
	    } else {
	        // Si el usuario se elimina, registramos el estado y la fecha de borrado en Firestore
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
	            "Usuario ID: " + idUsuario +
	                    " cambio de estado a " + estado.name() +
	                    ". Fecha: " + updateResult.getUpdateTime().toDate(),
	            false
	    );
	}

}