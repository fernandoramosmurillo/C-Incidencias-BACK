package com.cIncidencias.api.servicios;

import com.cIncidencias.api.modelos.Comentario;
import com.cIncidencias.api.repositorios.ComentarioRepository;
import com.cIncidencias.api.excepciones.NullParamsException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Servicio para gestionar la lógica de los comentarios.
 * Implementa IGenericoService para asegurar un flujo estandarizado de validación y persistencia.
 */
@Service
public class ComentarioService implements IGenericoService<Comentario> {

	private final ComentarioRepository comentarioRepository;

	public ComentarioService(ComentarioRepository comentarioRepository) {
		this.comentarioRepository = comentarioRepository;
	}

	/**
	 * Valida y registra un nuevo comentario. 
	 * Verifica que el objeto no sea nulo y que el texto contenga caracteres visibles.
	 * * @param comentario Objeto Comentario a guardar.
	 * @throws NullParamsException Si el comentario es nulo o el texto está vacío.
	 * @throws ExecutionException Si ocurre un error en la ejecución de la tarea en Firestore.
	 * @throws InterruptedException Si se interrumpe el hilo durante la comunicación.
	 * @throws IOException Si falla la escritura del log local.
	 */
	@Override
	public void guardar(Comentario comentario) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (comentario == null) {
			throw new NullParamsException("Se necesita un comentario para publicarlo");
		}
		
		if (comentario.getTexto() == null || comentario.getTexto().trim().isEmpty()) {
			throw new NullParamsException("El texto del comentario no puede estar vacío.");
		}
		
		comentarioRepository.guardar(comentario);
	}

	/**
	 * Recupera todos los comentarios almacenados en Firestore.
	 * * @return Lista de objetos Comentario.
	 * @throws ExecutionException Si hay un fallo al recuperar los datos del servidor.
	 * @throws InterruptedException Si se interrumpe la conexión durante la consulta.
	 */
	@Override
	public List<Comentario> obtenerTodos() throws ExecutionException, InterruptedException {
		return comentarioRepository.obtenerTodos();
	}

	/**
	 * Busca un comentario específico mediante su identificador único.
	 * * @param idComentario El identificador del comentario.
	 * @return El Comentario encontrado o null si no existe.
	 * @throws NullParamsException Si el ID proporcionado es nulo.
	 * @throws ExecutionException Si la consulta falla en el servidor.
	 * @throws InterruptedException Si se corta la comunicación con Firestore.
	 */
	@Override
	public Comentario obtenerPorId(String idComentario) throws NullParamsException, ExecutionException, InterruptedException {
		if (idComentario == null) {
			throw new NullParamsException("Es necesario un ID para buscar el comentario.");
		}
		return comentarioRepository.obtenerPorId(idComentario);
	}

	/**
	 * Elimina un comentario del sistema tras validar que el ID no es nulo.
	 * * @param idComentario ID del comentario a borrar.
	 * @throws NullParamsException Si el ID es nulo.
	 * @throws ExecutionException Si el borrado falla en la base de datos.
	 * @throws InterruptedException Si el proceso es interrumpido.
	 * @throws IOException Si ocurre un error al registrar la acción en el log.
	 */
	@Override
	public void eliminar(String idComentario) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (idComentario == null) {
			throw new NullParamsException("Es necesario un ID para eliminar el comentario.");
		}
		comentarioRepository.eliminar(idComentario);
	}

	/**
	 * Actualiza la información de un comentario existente.
	 * * @param comentario Objeto con los datos actualizados.
	 * @throws NullParamsException Si el objeto o su ID son nulos.
	 * @throws ExecutionException Si la actualización falla en Firestore.
	 * @throws InterruptedException Si se interrumpe la tarea asíncrona.
	 * @throws IOException Si falla el registro en el log local.
	 */
	@Override
	public void modificar(Comentario comentario) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (comentario == null || comentario.getIdComentario() == null) {
			throw new NullParamsException("Datos insuficientes para modificar el comentario.");
		}
		comentarioRepository.modificar(comentario);
	}
}