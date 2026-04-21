package com.cIncidencias.api.servicios;

import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.modelos.Usuario;
import com.cIncidencias.api.repositorios.UsuarioRepository;
import com.cIncidencias.api.excepciones.NullParamsException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Servicio para gestionar la lógica de los usuarios (ciudadanos, operarios y administradores).
 * Implementa IGenericoService para asegurar la integridad de los perfiles en el sistema.
 * Esta capa centraliza las reglas de validación de identidad y el control de estados de cuenta.
 */
@Service
public class UsuarioService implements IGenericoService<Usuario> {

	private final UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	/**
	 * Valida y registra un nuevo usuario en Firestore.
	 * Verifica que el nombre, apellidos y email sean válidos antes de persistir la información.
	 * @param usuario Objeto Usuario con la información de perfil.
	 * @throws NullParamsException Si faltan datos obligatorios o el formato de email es incorrecto.
	 * @throws ExecutionException Si ocurre un error en la tarea asíncrona de Firestore.
	 * @throws InterruptedException Si se interrumpe el hilo de ejecución.
	 * @throws IOException Si falla el registro en el log local.
	 */
	@Override
	public void guardar(Usuario usuario) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (usuario == null) {
			throw new NullParamsException("No se puede registrar un usuario nulo.");
		}
		
		if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
			throw new NullParamsException("El nombre del usuario es obligatorio.");
		}
		
		if (usuario.getCorreoElectronico() == null || !usuario.getCorreoElectronico().contains("@")) {
			throw new NullParamsException("El email proporcionado no es válido.");
		}

		usuarioRepository.guardar(usuario);
	}

	/**
	 * Recupera el listado completo de usuarios registrados en la plataforma.
	 * @return Lista de objetos Usuario.
	 * @throws ExecutionException Si hay un fallo al obtener los datos del servidor.
	 * @throws InterruptedException Si se interrumpe la conexión con Firestore.
	 */
	@Override
	public List<Usuario> obtenerTodos() throws ExecutionException, InterruptedException {
		return usuarioRepository.obtenerTodos();
	}

	/**
	 * Busca un usuario específico mediante su identificador único (UID).
	 * @param idUsuario Identificador único del usuario.
	 * @return El objeto Usuario encontrado o null si no existe.
	 * @throws NullParamsException Si el ID es nulo o está vacío.
	 * @throws ExecutionException Si la consulta falla en el servidor.
	 * @throws InterruptedException Si se corta la comunicación asíncrona.
	 */
	@Override
	public Usuario obtenerPorId(String idUsuario) throws NullParamsException, ExecutionException, InterruptedException {
		if (idUsuario == null || idUsuario.trim().isEmpty()) {
			throw new NullParamsException("Se requiere un ID válido para buscar al usuario.");
		}
		return usuarioRepository.obtenerPorId(idUsuario);
	}

	/**
	 * Elimina permanentemente el perfil de un usuario del sistema tras validar su ID.
	 * @param idUsuario ID del usuario a eliminar.
	 * @throws NullParamsException Si el ID es nulo o está vacío.
	 * @throws ExecutionException Si el borrado falla en la base de datos.
	 * @throws InterruptedException Si el proceso es interrumpido.
	 * @throws IOException Si ocurre un error al registrar la acción en el log.
	 */
	@Override
	public void eliminar(String idUsuario) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (idUsuario == null || idUsuario.trim().isEmpty()) {
			throw new NullParamsException("Es necesario un ID válido para eliminar al usuario.");
		}
		usuarioRepository.eliminar(idUsuario);
	}

	/**
	 * Actualiza la información de perfil de un usuario existente.
	 * @param usuario Objeto con los datos actualizados e ID válido.
	 * @throws NullParamsException Si el objeto o su ID son nulos o vacíos.
	 * @throws ExecutionException Si la actualización falla en Firestore.
	 * @throws InterruptedException Si se interrumpe la comunicación.
	 * @throws IOException Si falla la escritura del log de incidencias.
	 */
	@Override
	public void modificar(Usuario usuario) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (usuario == null || usuario.getIdUsuario() == null || usuario.getIdUsuario().trim().isEmpty()) {
			throw new NullParamsException("Datos insuficientes para modificar el perfil de usuario (ID ausente).");
		}
		usuarioRepository.modificar(usuario);
	}
	
	/**
	 * Gestiona el cambio de estado de la cuenta de usuario (ej: Activo, Bloqueado o Eliminado lógico).
	 * Valida la identidad del usuario antes de proceder con el cambio en el repositorio.
	 */
	@Override
	public void cambiarEstado(String idUsuario, ModeloBase.Estados estado) throws Exception {

	    if (idUsuario == null || idUsuario.trim().isEmpty()) {
	        throw new NullParamsException("Se necesita un ID válido para cambiar el estado del usuario.");
	    }

	    usuarioRepository.cambiarEstado(idUsuario, estado);
	}
}