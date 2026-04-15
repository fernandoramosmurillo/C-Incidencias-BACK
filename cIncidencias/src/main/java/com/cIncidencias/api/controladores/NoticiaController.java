package com.cIncidencias.api.controladores;

import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.modelos.Noticia;
import com.cIncidencias.api.servicios.IGenericoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de noticias y comunicados oficiales.
 * Conecta las peticiones externas con la lógica de negocio de NoticiaService.
 */
@RestController
@RequestMapping("/api/noticias")
public class NoticiaController {

	private final IGenericoService<Noticia> noticiaService;

	public NoticiaController(IGenericoService<Noticia> noticiaService) {
		this.noticiaService = noticiaService;
	}

	/**
	 * Obtiene el listado de todas las noticias publicadas.
	 * GET /api/noticias
	 */
	@GetMapping
	public ResponseEntity<List<Noticia>> listar() {
		try {
			List<Noticia> lista = noticiaService.obtenerTodos();
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Busca una noticia por su ID único.
	 * GET /api/noticias/{id}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Noticia> obtenerPorId(@PathVariable("id") String id) {
		try {
			Noticia noticia = noticiaService.obtenerPorId(id);
			return (noticia != null) 
					? new ResponseEntity<>(noticia, HttpStatus.OK) 
					: new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Publica una nueva noticia.
	 * POST /api/noticias
	 */
	@PostMapping
	public ResponseEntity<String> guardar(@RequestBody Noticia noticia) {
		try {
			noticiaService.guardar(noticia);
			return new ResponseEntity<>("Noticia publicada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * ¡Advertencia!
	 * Este método solo debe usarse durante las pruebas y desarrollo.
	 * * Crea una lista de noticias de forma masiva.
	 * POST /api/noticias/guardarLista
	 */
	@PostMapping("/guardarLista")
	public ResponseEntity<String> guardarLista(@RequestBody List<Noticia> listaNoticias) {
		try {
			for (Noticia noticia : listaNoticias) {
				noticiaService.guardar(noticia);
			}
			return new ResponseEntity<>("Carga masiva de noticias registrada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Actualiza el contenido de una noticia existente.
	 * PUT /api/noticias
	 */
	@PutMapping
	public ResponseEntity<String> modificar(@RequestBody Noticia noticia) {
		try {
			noticiaService.modificar(noticia);
			return new ResponseEntity<>("Noticia actualizada correctamente", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Elimina una noticia por su identificador.
	 * DELETE /api/noticias/{id}
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") String id) {
		try {
			noticiaService.eliminar(id);
			return new ResponseEntity<>("Noticia eliminada correctamente", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Cambia el estado de la noticia (Activo, Inactivo, Eliminado, etc.)
	 * PUT /api/noticias/{id}/estado/{estado}
	 */
	@PutMapping("/{id}/estado/{estado}")
	public ResponseEntity<String> cambiarEstado(@PathVariable String id, @PathVariable ModeloBase.Estados estado) {
	    try {
	        noticiaService.cambiarEstado(id, estado);
	        return new ResponseEntity<>("Estado de la noticia actualizado a: " + estado, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
}