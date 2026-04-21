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
@CrossOrigin(origins = "*")
public class NotificacionController {

	private final IGenericoService<Notificacion> notificacionService;

	/**
	 * Constructor para inyectar el servicio. Spring se encarga de todo el lío.
	 */
	public NotificacionController(IGenericoService<Notificacion> notificacionService) {
		this.notificacionService = notificacionService;
	}

	/**
	 * Recupera todas las notificaciones del sistema. 
	 * Se usa sobre todo para que los administradores vean el histórico de avisos.
	 */
	@GetMapping
	public ResponseEntity<List<Notificacion>> listar() {
		try {
			List<Notificacion> lista = notificacionService.obtenerTodos();
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Busca un aviso concreto por su ID para ver los detalles.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Notificacion> obtenerPorId(@PathVariable("id") String id) {
		try {
			Notificacion notificacion = notificacionService.obtenerPorId(id);
			return (notificacion != null) 
					? new ResponseEntity<>(notificacion, HttpStatus.OK) 
					: new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Lanza una nueva notificación. Se usa para avisar a los vecinos o a los técnicos.
	 */
	@PostMapping
	public ResponseEntity<String> guardar(@RequestBody Notificacion notificacion) {
		try {
			notificacionService.guardar(notificacion);
			return new ResponseEntity<>("Notificación enviada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * ¡Advertencia! Este método es solo para meter datos de prueba rápido.
	 * Registra una lista de notificaciones de forma masiva.
	 */
	@PostMapping("/guardarLista")
	public ResponseEntity<String> guardarLista(@RequestBody List<Notificacion> listaNotificaciones) {
		try {
			for (Notificacion notificacion : listaNotificaciones) {
				notificacionService.guardar(notificacion);
			}
			return new ResponseEntity<>("Carga masiva de notificaciones completada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Sirve para retocar una notificación, como cuando queremos cambiar un texto o estado.
	 */
	@PutMapping
	public ResponseEntity<String> modificar(@RequestBody Notificacion notificacion) {
		try {
			notificacionService.modificar(notificacion);
			return new ResponseEntity<>("Notificación actualizada correctamente", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Borra el aviso del sistema definitivamente usando su identificador.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") String id) {
		try {
			notificacionService.eliminar(id);
			return new ResponseEntity<>("Notificación eliminada", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}