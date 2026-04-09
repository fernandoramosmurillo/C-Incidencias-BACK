package com.cIncidencias.api.servicios;

import com.cIncidencias.api.modelos.Comentario;
import com.cIncidencias.api.repositorios.ComentarioRepository;
import com.cIncidencias.api.excepciones.NullParamsException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para gestionar la lógica de los comentarios.
 * Se encarga de validar los datos antes de persistirlos en el repositorio.
 */
@Service
public class ComentarioService {

	private final ComentarioRepository comentarioRepository;

	public ComentarioService(ComentarioRepository comentarioRepository) {
		this.comentarioRepository = comentarioRepository;
	}

	/**
	 * Registra un nuevo comentario validando que tenga contenido y referencias.
	 * @param comentario Objeto con la información del comentario.
	 * @throws Exception Si faltan datos obligatorios o hay error en la base de datos.
	 */
	public void publicarComentario(Comentario comentario) throws Exception {
		
		// Validaciones de valores
		if (comentario == null) {
			throw new NullParamsException("Se necesita un comentario para publicarlo");
		}
		
		if (comentario.getTexto() == null || comentario.getTexto().trim().isEmpty()) {
			throw new NullParamsException("El texto del comentario no puede estar vacío.");
		}
		
		comentarioRepository.guardarComentario(comentario);
	}

	/**
	 * Obtiene todos los comentarios registrados.
	 */
	public List<Comentario> obtenerTodos() throws Exception {
		return comentarioRepository.obtenerComentarios();
	}

	/**
	 * Busca un comentario por su ID único.
	 */
	public Comentario obtenerPorId(String idComentario) throws Exception {
		if (idComentario == null) {
			throw new NullParamsException("Es necesario un ID para buscar el comentario.");
		}
		return comentarioRepository.obtenerComentario(idComentario);
	}

	/**
	 * Elimina un comentario del sistema.
	 */
	public void borrarComentario(String idComentario) throws Exception {
		if (idComentario == null) {
			throw new NullParamsException("Es necesario un ID para eliminar el comentario.");
		}
		comentarioRepository.eliminarComentario(idComentario);
	}
}