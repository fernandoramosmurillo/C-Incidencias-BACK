package com.cIncidencias.api.repositorios;

import com.cIncidencias.api.ficheros.ManejadorFicheros;
import com.cIncidencias.api.modelos.Administrador;
import com.cIncidencias.api.modelos.Ciudadano;
import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.modelos.ModeloBase.Estados;
import com.cIncidencias.api.modelos.OperarioMunicipal;
import com.cIncidencias.api.modelos.Usuario;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Repositorio para la gestión de usuarios en Firestore. Implementa
 * IGenericoRepository para estandarizar la persistencia de ciudadanos, operarios y admins.
 * Se encarga de centralizar las operaciones sobre la colección central de perfiles.
 */
@Repository
public class UsuarioRepository implements IGenericoRepository<Usuario> {

	private File archivoLog = new File("logs");
	private final Firestore FIRESTORE;
	private final String COLECCION = "usuarios";

	public UsuarioRepository(Firestore firestore) {
		this.FIRESTORE = firestore;
	}

	/**
	 * Registra un nuevo usuario en Firestore.
	 * Además de la persistencia, deja constancia en el log local del nombre completo
	 * del usuario para facilitar el rastreo administrativo.
	 */
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

	/**
	 * Recupera todos los usuarios registrados en el sistema.
	 */
	@Override
	public List<Usuario> obtenerTodos() throws InterruptedException, ExecutionException {
	    List<QueryDocumentSnapshot> docs = FIRESTORE.collection(COLECCION).get().get().getDocuments();
	    List<Usuario> lista = new ArrayList<>();

	    for (QueryDocumentSnapshot doc : docs) {
	        String rol = doc.getString("rolUsuario");
	        if ("CIUDADANO".equals(rol)) lista.add(doc.toObject(Ciudadano.class));
	        else if ("OPERARIO".equals(rol)) lista.add(doc.toObject(OperarioMunicipal.class));
	        else if ("ADMINISTRADOR".equals(rol)) lista.add(doc.toObject(Administrador.class));
	        else lista.add(doc.toObject(Usuario.class));
	    }
	    return lista;
	}

	/**
	 * Busca un perfil de usuario mediante su identificador único (UID).
	 */
	@Override
	public Usuario obtenerPorId(String idUsuario) throws InterruptedException, ExecutionException {
		DocumentSnapshot doc = FIRESTORE.collection(COLECCION).document(idUsuario).get().get();
	    
	    if (!doc.exists()) return null;

	    String rol = doc.getString("rolUsuario");
	    if ("CIUDADANO".equals(rol)) return doc.toObject(Ciudadano.class);
	    if ("OPERARIO".equals(rol)) return doc.toObject(OperarioMunicipal.class);
	    if ("ADMINISTRADOR".equals(rol)) return doc.toObject(Administrador.class);
	    
	    return doc.toObject(Usuario.class);
	}

	/**
	 * Elimina permanentemente la cuenta de usuario de la base de datos de Firestore.
	 */
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

	/**
	 * Actualiza los datos de perfil de un usuario existente.
	 */
	@Override
	public void modificar(Usuario usuario) throws ExecutionException, InterruptedException, IOException {
		DocumentReference docRef = FIRESTORE.collection(COLECCION).document(usuario.getIdUsuario());
		ApiFuture<WriteResult> result = docRef.set(usuario);

		// Cambiado a .toDate() para consistencia local
		Object fechaActualizacion = result.get().getUpdateTime().toDate();

		ManejadorFicheros.escribir("logs/incidencias.log", "Usuario ID: " + usuario.getIdUsuario() + " ["
				+ usuario.getNombre() + "] modificado correctamente. Fecha: " + fechaActualizacion, false);
	}

	/**
	 * Gestiona cambios en el estado de la cuenta (por ejemplo, BLOQUEADO por seguridad).
	 * En caso de marcarse como ELIMINADO (borrado lógico), se actualiza automáticamente
	 * la marca de tiempo de eliminación en el documento.
	 */
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
	
	/**
	 * Verifica si un usuario ya existe en la base de datos mediante su DNI.
	 * Se asume que el DNI es el ID del documento en Firestore.
	 * @param dni El documento de identidad a comprobar.
	 * @return true si ya existe, false si está disponible.
	 */
	public boolean existePorDni(String dni) throws InterruptedException, ExecutionException {
	    DocumentReference docRef = FIRESTORE.collection(COLECCION).document(dni);
	    ApiFuture<DocumentSnapshot> query = docRef.get();
	    
	    // Si el documento existe en Firestore, retornamos true
	    return query.get().exists();
	}

	/**
	 * Verifica si un correo electrónico ya está registrado.
	 * Se utiliza el email como ID del documento para mantener la coherencia.
	 * @param email El correo a comprobar.
	 * @return true si ya existe, false si está disponible.
	 */
	public boolean existePorEmail(String email) throws InterruptedException, ExecutionException {
	    DocumentReference docRef = FIRESTORE.collection(COLECCION).document(email);
	    ApiFuture<DocumentSnapshot> query = docRef.get();
	    
	    // Si el documento existe en Firestore, retornamos true
	    return query.get().exists();
	}
}