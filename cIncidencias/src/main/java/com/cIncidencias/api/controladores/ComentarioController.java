package com.cIncidencias.api.controladores;

import com.cIncidencias.api.modelos.Comentario;
import com.cIncidencias.api.servicios.IGenericoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de comentarios. Conecta las peticiones
 * externas con la lógica de negocio de ComentarioService.
 */
@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

	private final IGenericoService<Comentario> comentarioService;

	public ComentarioController(IGenericoService<Comentario> comentarioService) {
		this.comentarioService = comentarioService;
	}

	/**
	 * Obtiene el listado de todos los comentarios registrados. GET /api/comentarios
	 */
	@GetMapping
	public ResponseEntity<List<Comentario>> listar() {
		try {
			List<Comentario> lista = comentarioService.obtenerTodos();
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Busca un comentario específico por su ID. GET /api/comentarios/{id}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Comentario> obtenerPorId(@PathVariable("id") String id) {
		try {
			Comentario comentario = comentarioService.obtenerPorId(id);
			return (comentario != null) ? new ResponseEntity<>(comentario, HttpStatus.OK)
					: new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Crea un nuevo comentario. POST /api/comentarios
	 */
	@PostMapping
	public ResponseEntity<String> guardar(@RequestBody Comentario comentario) {
		try {
			comentarioService.guardar(comentario);
			return new ResponseEntity<>("Comentario registrado con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * ¡Advertencia! Este método solo debe usarse durante las pruebas y desarrollo.
	 * * Crea una lista de nuevos comentarios. POST /api/comentarios/guardarLista
	 */
	@PostMapping("/guardarLista")
	public ResponseEntity<String> guardarLista(@RequestBody List<Comentario> listaComentarios) {
		try {
			for (Comentario comentario : listaComentarios) {
				comentarioService.guardar(comentario);
			}
			return new ResponseEntity<>("Lista de comentarios registrada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Actualiza los datos de un comentario existente. PUT /api/comentarios
	 */
	@PutMapping
	public ResponseEntity<String> modificar(@RequestBody Comentario comentario) {
		try {
			comentarioService.modificar(comentario);
			return new ResponseEntity<>("Comentario actualizado correctamente", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Elimina un comentario por su ID. DELETE /api/comentarios/{id}
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") String id) {
		try {
			comentarioService.eliminar(id);
			return new ResponseEntity<>("Comentario eliminado", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Elimina un comentario por su ID temporalmente.
	 */

	@PutMapping("/estado/eliminarTemporalmente/{id}")
	public ResponseEntity<String> eliminarTemporalmente(@PathVariable String id) {
		try {
			comentarioService.eliminarTemporalmente(id);
			return new ResponseEntity<>("Comentario marcado como ELIMINADO", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}