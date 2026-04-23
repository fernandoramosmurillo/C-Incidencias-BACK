package com.cIncidencias.api.servicios;

import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.modelos.Noticia;
import com.cIncidencias.api.repositorios.NoticiaRepository;
import com.cIncidencias.api.excepciones.NullParamsException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Servicio para gestionar la lógica de las noticias y comunicados oficiales.
 * Implementa IGenericoService para asegurar que la información municipal sea válida.
 * Esta capa actúa como moderadora, asegurando que los comunicados cumplan con los requisitos
 * mínimos de contenido antes de ser visibles para la ciudadanía.
 */
@Service
public class NoticiaService implements IGenericoService<Noticia> {

	private final NoticiaRepository noticiaRepository;

	public NoticiaService(NoticiaRepository noticiaRepository) {
		this.noticiaRepository = noticiaRepository;
	}

	/**
	 * Valida y publica una nueva noticia. 
	 * Verifica que el objeto no sea nulo y que tenga título y cuerpo de contenido.
	 * @param noticia Objeto Noticia a guardar.
	 * @throws NullParamsException Si la noticia es nula o le faltan campos obligatorios.
	 * @throws ExecutionException Si ocurre un error en la persistencia de Firestore.
	 * @throws InterruptedException Si se interrumpe el hilo durante la operación.
	 * @throws IOException Si falla la escritura del log local.
	 */
	@Override
	public void guardar(Noticia noticia) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (noticia == null) {
			throw new NullParamsException("No se puede publicar una noticia inexistente.");
		}
		
		if (noticia.getTitulo() == null || noticia.getTitulo().trim().isEmpty()) {
			throw new NullParamsException("El título de la noticia no puede estar vacío.");
		}
		
		if (noticia.getCuerpo() == null || noticia.getCuerpo().trim().isEmpty()) {
			throw new NullParamsException("El cuerpo de la noticia no puede estar vacío.");
		}
		
		noticiaRepository.guardar(noticia);
	}

	/**
	 * Recupera todas las noticias registradas en Firestore.
	 * @return Lista de objetos Noticia.
	 * @throws ExecutionException Si hay un fallo al recuperar los datos del servidor.
	 * @throws InterruptedException Si se interrumpe la conexión con Firestore.
	 */
	@Override
	public List<Noticia> obtenerTodos() throws ExecutionException, InterruptedException {
		return noticiaRepository.obtenerTodos();
	}

	/**
	 * Busca una noticia específica por su identificador único.
	 * @param idNoticia Identificador de la noticia.
	 * @return La noticia encontrada o null si no existe.
	 * @throws NullParamsException Si el ID proporcionado es nulo o está vacío.
	 * @throws ExecutionException Si la consulta falla en el servidor.
	 * @throws InterruptedException Si se corta la comunicación.
	 */
	@Override
	public Noticia obtenerPorId(String idNoticia) throws NullParamsException, ExecutionException, InterruptedException {
		if (idNoticia == null || idNoticia.trim().isEmpty()) {
			throw new NullParamsException("Es necesario un ID válido para buscar la noticia.");
		}
		return noticiaRepository.obtenerPorId(idNoticia);
	}

	/**
	 * Elimina una noticia del sistema.
	 * @param idNoticia ID de la noticia a borrar.
	 * @throws NullParamsException Si el ID es nulo o está vacío.
	 * @throws ExecutionException Si el borrado falla en la base de datos.
	 * @throws InterruptedException Si el proceso es interrumpido.
	 * @throws IOException Si ocurre un error al registrar la acción en el log.
	 */
	@Override
	public void eliminar(String idNoticia) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (idNoticia == null || idNoticia.trim().isEmpty()) {
			throw new NullParamsException("Es necesario un ID válido para eliminar la noticia.");
		}
		noticiaRepository.eliminar(idNoticia);
	}

	/**
	 * Actualiza la información de una noticia existente.
	 * @param noticia Objeto con los datos actualizados.
	 * @throws NullParamsException Si el objeto o su ID son nulos.
	 * @throws ExecutionException Si la actualización falla en Firestore.
	 * @throws InterruptedException Si se interrumpe la tarea asíncrona.
	 * @throws IOException Si falla el registro en el log local.
	 */
	@Override
	public void modificar(Noticia noticia) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (noticia == null || noticia.getIdNoticia() == null || noticia.getIdNoticia().trim().isEmpty()) {
			throw new NullParamsException("Datos insuficientes para modificar la noticia.");
		}
		noticiaRepository.modificar(noticia);
	}
	
	/**
	 * Gestiona el cambio de estado de la noticia (activación, desactivación o borrado lógico).
	 * Valida que el identificador sea correcto antes de delegar en el repositorio.
	 */
	@Override
	public void cambiarEstado(String idNoticia, ModeloBase.Estados estado) throws Exception {

	    if (idNoticia == null || idNoticia.trim().isEmpty()) {
	        throw new NullParamsException("Se necesita un ID válido para cambiar el estado de la noticia.");
	    }

	    noticiaRepository.cambiarEstado(idNoticia, estado);
	}
}