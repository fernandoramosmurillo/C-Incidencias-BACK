package com.cIncidencias.api.controladores;

import com.cIncidencias.api.modelos.Notificacion;
import com.cIncidencias.api.servicios.IGenericoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de notificaciones y alertas.
 * Facilita el envío y seguimiento de avisos a los usuarios del sistema.
 */
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

	private final IGenericoService<Notificacion> notificacionService;

	public NotificacionController(IGenericoService<Notificacion> notificacionService) {
		this.notificacionService = notificacionService;
	}

	/**
	 * Obtiene todas las notificaciones registradas.
	 * GET /api/notificaciones
	 */
	@GetMapping
	public ResponseEntity<List<Notificacion>> listar() {
		try {
			List<Notificacion> lista = notificacionService.obtenerTodos();
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Busca una notificación por su ID.
	 * GET /api/notificaciones/{id}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Notificacion> obtenerPorId(@PathVariable("id") String id) {
		try {
			Notificacion notificacion = notificacionService.obtenerPorId(id);
			return (notificacion != null) 
					? new ResponseEntity<>(notificacion, HttpStatus.OK) 
					: new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Envía/Guarda una nueva notificación.
	 * POST /api/notificaciones
	 */
	@PostMapping
	public ResponseEntity<String> guardar(@RequestBody Notificacion notificacion) {
		try {
			notificacionService.guardar(notificacion);
			return new ResponseEntity<>("Notificación enviada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * ¡Advertencia!
	 * Este método solo debe usarse durante las pruebas y desarrollo.
	 * * Registra una lista de notificaciones de forma masiva.
	 * POST /api/notificaciones/guardarLista
	 */
	@PostMapping("/guardarLista")
	public ResponseEntity<String> guardarLista(@RequestBody List<Notificacion> listaNotificaciones) {
		try {
			for (Notificacion notificacion : listaNotificaciones) {
				notificacionService.guardar(notificacion);
			}
			return new ResponseEntity<>("Carga masiva de notificaciones completada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Modifica una notificación (ej. para marcarla como leída).
	 * PUT /api/notificaciones
	 */
	@PutMapping
	public ResponseEntity<String> modificar(@RequestBody Notificacion notificacion) {
		try {
			notificacionService.modificar(notificacion);
			return new ResponseEntity<>("Notificación actualizada correctamente", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Elimina una notificación del sistema.
	 * DELETE /api/notificaciones/{id}
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") String id) {
		try {
			notificacionService.eliminar(id);
			return new ResponseEntity<>("Notificación eliminada", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}