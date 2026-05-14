package com.cIncidencias.api.servicios;

import com.cIncidencias.api.modelos.Ciudadano;
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
	 * Valida y registra un nuevo usuario.
	 * Se realiza un casteo a Ciudadano para verificar el DNI si el objeto lo permite.
	 */
	@Override
	public void guardar(Usuario usuario) throws NullParamsException, ExecutionException, InterruptedException, IOException {
	    if (usuario == null) {
	        throw new NullParamsException("No se puede registrar un usuario nulo.");
	    }

	    // Solo validamos DNI si el usuario que llega es realmente un Ciudadano
	    if (usuario instanceof Ciudadano) {
	        Ciudadano ciudadano = (Ciudadano) usuario;
	        if (ciudadano.getDni() == null || usuarioRepository.existePorDni(ciudadano.getDni())) {
	            throw new NullParamsException("El DNI ya se encuentra registrado o no es válido.");
	        }
	    }

	    // Validaciones comunes para todos (Ciudadano, Operario, Admin)
	    if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
	        throw new NullParamsException("El nombre del usuario es obligatorio.");
	    }
	    
	    if (usuario.getCorreoElectronico() == null || !usuario.getCorreoElectronico().contains("@")) {
	        throw new NullParamsException("El email proporcionado no es válido.");
	    }
	    
	    if (usuarioRepository.existePorEmail(usuario.getCorreoElectronico())) {
	        throw new NullParamsException("El correo electrónico ya está en uso.");
	    }

	    usuarioRepository.guardar(usuario);
	}

	@Override
	public List<Usuario> obtenerTodos() throws ExecutionException, InterruptedException {
		return usuarioRepository.obtenerTodos();
	}

	@Override
	public Usuario obtenerPorId(String idUsuario) throws NullParamsException, ExecutionException, InterruptedException {
		if (idUsuario == null || idUsuario.trim().isEmpty()) {
			throw new NullParamsException("Se requiere un ID válido para buscar al usuario.");
		}
		return usuarioRepository.obtenerPorId(idUsuario);
	}

	@Override
	public void eliminar(String idUsuario) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (idUsuario == null || idUsuario.trim().isEmpty()) {
			throw new NullParamsException("Es necesario un ID válido para eliminar al usuario.");
		}

		// Verificamos si el usuario existe antes de borrar su perfil
		if (!usuarioRepository.existePorDni(idUsuario)) { // Si usas el DNI como ID
			throw new NullParamsException("No se puede eliminar: el usuario no existe.");
		}

		usuarioRepository.eliminar(idUsuario);
	}

	@Override
	public void modificar(Usuario usuario) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (usuario == null || usuario.getIdUsuario() == null || usuario.getIdUsuario().trim().isEmpty()) {
			throw new NullParamsException("Datos insuficientes para modificar el perfil de usuario (ID ausente).");
		}

		// Validamos existencia antes de modificar
		if (!usuarioRepository.existePorDni(usuario.getIdUsuario())) {
			throw new NullParamsException("No se puede modificar un usuario que no existe.");
		}

		usuarioRepository.modificar(usuario);
	}
	
	@Override
	public void cambiarEstado(String idUsuario, ModeloBase.Estados estado) throws Exception {
		if (idUsuario == null || idUsuario.trim().isEmpty()) {
			throw new NullParamsException("Se necesita un ID válido para cambiar el estado del usuario.");
		}

		if (!usuarioRepository.existePorDni(idUsuario)) {
			throw new NullParamsException("No se encontró el usuario para actualizar su estado.");
		}

		usuarioRepository.cambiarEstado(idUsuario, estado);
	}
}