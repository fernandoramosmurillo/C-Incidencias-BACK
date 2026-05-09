package com.cIncidencias.api.controladores;

import com.cIncidencias.api.modelos.Comentario;
import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.servicios.FirebaseAuthService;
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
@CrossOrigin(origins = "*")
public class ComentarioController {

	private final IGenericoService<Comentario> comentarioService;
	private final FirebaseAuthService authService;

	/**
	 * Inyectamos el servicio genérico para manejar los comentarios y el servicio de autenticación.
	 */
	public ComentarioController(IGenericoService<Comentario> comentarioService, FirebaseAuthService authService) {
		this.comentarioService = comentarioService;
		this.authService = authService;
	}

	/**
	 * Obtiene el listado de todos los comentarios registrados. GET /api/comentarios
	 */
	@GetMapping
	public ResponseEntity<List<Comentario>> listar(@RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			List<Comentario> lista = comentarioService.obtenerTodos();
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Busca un comentario específico por su ID. GET /api/comentarios/{id}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Comentario> obtenerPorId(@PathVariable("id") String id, @RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			Comentario comentario = comentarioService.obtenerPorId(id);
			return (comentario != null) ? new ResponseEntity<>(comentario, HttpStatus.OK)
					: new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Crea un nuevo comentario. POST /api/comentarios
	 */
	@PostMapping
	public ResponseEntity<String> guardar(@RequestBody Comentario comentario, @RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			comentarioService.guardar(comentario);
			return new ResponseEntity<>("Comentario registrado con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Este método se mantiene público para volcar datos de prueba rápidamente. 
	 * POST /api/comentarios/guardarLista
	 */
	@PostMapping("/guardarLista")
	public ResponseEntity<String> guardarLista(@RequestBody List<Comentario> listaComentarios) {
		try {
			for (Comentario comentario : listaComentarios) {
				comentarioService.guardar(comentario);
			}
			return new ResponseEntity<>("Lista de comentarios registrada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Actualiza los datos de un comentario existente. PUT /api/comentarios
	 */
	@PutMapping
	public ResponseEntity<String> modificar(@RequestBody Comentario comentario, @RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			comentarioService.modificar(comentario);
			return new ResponseEntity<>("Comentario actualizado correctamente", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Elimina un comentario por su ID. DELETE /api/comentarios/{id}
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") String id, @RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			comentarioService.eliminar(id);
			return new ResponseEntity<>("Comentario eliminado", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Cambia el estado del comentario (Activo, Inactivo, Eliminado, etc.) 
	 * sin necesidad de borrar el registro completo.
	 */
	@PutMapping("/{id}/estado/{estado}")
	public ResponseEntity<String> cambiarEstado(@PathVariable String id, @PathVariable ModeloBase.Estados estado, @RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			comentarioService.cambiarEstado(id, estado);
			return new ResponseEntity<>("Estado del comentario actualizado a: " + estado, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}