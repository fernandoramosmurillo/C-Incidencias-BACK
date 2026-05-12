package com.cIncidencias.api.controladores;

import com.cIncidencias.api.modelos.Notificacion;
import com.cIncidencias.api.servicios.FirebaseAuthService;
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
	private final FirebaseAuthService authService;

	/**
	 * Constructor para inyectar el servicio de notificaciones y el de seguridad.
	 */
	public NotificacionController(IGenericoService<Notificacion> notificacionService, FirebaseAuthService authService) {
		this.notificacionService = notificacionService;
		this.authService = authService;
	}

	/**
	 * Recupera todas las notificaciones del sistema. 
	 * Requiere token de autorización.
	 */
	@GetMapping
	public ResponseEntity<List<Notificacion>> listar(@RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			List<Notificacion> lista = notificacionService.obtenerTodos();
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Busca un aviso concreto por su ID para ver los detalles.
	 * Requiere token de autorización.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Notificacion> obtenerPorId(@PathVariable("id") String id, @RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
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
	 * Requiere token de autorización.
	 */
	@PostMapping
	public ResponseEntity<String> guardar(@RequestBody Notificacion notificacion, @RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			notificacionService.guardar(notificacion);
			return new ResponseEntity<>("Notificación enviada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * ¡Advertencia! Este método es solo para meter datos de prueba rápido.
	 * Se mantiene público para facilitar el desarrollo.
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
	 * Sirve para retocar una notificación.
	 * Requiere token de autorización.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<String> modificar(
	        @PathVariable("id") String id, 
	        @RequestBody Notificacion notificacion, 
	        @RequestHeader("Authorization") String token) {
	    try {
	        String idToken = token.startsWith("Bearer ") ? token.substring(7) : token;
	        authService.verificarToken(idToken);
	        
	        notificacionService.modificar(notificacion);
	        
	        return new ResponseEntity<>("Notificación actualizada correctamente", HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	    }
	}

	/**
	 * Borra el aviso del sistema definitivamente usando su identificador.
	 * Requiere token de autorización.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") String id, @RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			notificacionService.eliminar(id);
			return new ResponseEntity<>("Notificación eliminada", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}