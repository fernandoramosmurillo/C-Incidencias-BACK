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

	@Override
	public void guardar(Noticia noticia) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (noticia == null || noticia.getIdNoticia() == null) {
			throw new NullParamsException("No se puede publicar una noticia inexistente o sin ID.");
		}
		
		// Verificación de duplicados antes de publicar
		if (noticiaRepository.existePorId(noticia.getIdNoticia())) {
			throw new NullParamsException("La noticia con ID " + noticia.getIdNoticia() + " ya existe.");
		}
		
		if (noticia.getTitulo() == null || noticia.getTitulo().trim().isEmpty()) {
			throw new NullParamsException("El título de la noticia no puede estar vacío.");
		}
		
		if (noticia.getCuerpo() == null || noticia.getCuerpo().trim().isEmpty()) {
			throw new NullParamsException("El cuerpo de la noticia no puede estar vacío.");
		}
		
		noticiaRepository.guardar(noticia);
	}

	@Override
	public List<Noticia> obtenerTodos() throws ExecutionException, InterruptedException {
		return noticiaRepository.obtenerTodos();
	}

	@Override
	public Noticia obtenerPorId(String idNoticia) throws NullParamsException, ExecutionException, InterruptedException {
		if (idNoticia == null || idNoticia.trim().isEmpty()) {
			throw new NullParamsException("Es necesario un ID válido para buscar la noticia.");
		}
		return noticiaRepository.obtenerPorId(idNoticia);
	}

	@Override
	public void eliminar(String idNoticia) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (idNoticia == null || idNoticia.trim().isEmpty()) {
			throw new NullParamsException("Es necesario un ID válido para eliminar la noticia.");
		}

		// Validamos que exista antes de proceder al borrado físico
		if (!noticiaRepository.existePorId(idNoticia)) {
			throw new NullParamsException("No se puede eliminar: la noticia no existe.");
		}

		noticiaRepository.eliminar(idNoticia);
	}

	@Override
	public void modificar(Noticia noticia) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (noticia == null || noticia.getIdNoticia() == null || noticia.getIdNoticia().trim().isEmpty()) {
			throw new NullParamsException("Datos insuficientes para modificar la noticia.");
		}

		// Comprobación de seguridad para asegurar que estamos editando algo real
		if (!noticiaRepository.existePorId(noticia.getIdNoticia())) {
			throw new NullParamsException("No se puede modificar una noticia que no existe en el sistema.");
		}

		noticiaRepository.modificar(noticia);
	}
	
	@Override
	public void cambiarEstado(String idNoticia, ModeloBase.Estados estado) throws Exception {
		if (idNoticia == null || idNoticia.trim().isEmpty()) {
			throw new NullParamsException("Se necesita un ID válido para cambiar el estado de la noticia.");
		}

		// Verificamos existencia antes de la actualización parcial en Firestore
		if (!noticiaRepository.existePorId(idNoticia)) {
			throw new NullParamsException("No se encontró la noticia para actualizar su estado.");
		}

		noticiaRepository.cambiarEstado(idNoticia, estado);
	}
}