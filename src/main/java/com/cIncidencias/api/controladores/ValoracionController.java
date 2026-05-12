package com.cIncidencias.api.controladores;

import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.modelos.Valoracion;
import com.cIncidencias.api.servicios.FirebaseAuthService;
import com.cIncidencias.api.servicios.IGenericoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de valoraciones de incidencias.
 * Permite a los ciudadanos dejar feedback sobre cómo se resolvieron sus reportes.
 */
@RestController
@RequestMapping("/api/valoraciones")
@CrossOrigin(origins = "*")
public class ValoracionController {

	private final IGenericoService<Valoracion> valoracionService;
	private final FirebaseAuthService authService;

	/**
	 * Constructor para inyectar el servicio de valoraciones y el de autenticación.
	 */
	public ValoracionController(IGenericoService<Valoracion> valoracionService, FirebaseAuthService authService) {
		this.valoracionService = valoracionService;
		this.authService = authService;
	}

	/**
	 * Recupera todas las reseñas y puntuaciones que han dejado los vecinos.
	 */
	@GetMapping
	public ResponseEntity<List<Valoracion>> listar(@RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			List<Valoracion> lista = valoracionService.obtenerTodos();
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Busca una valoración concreta por su ID.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Valoracion> obtenerPorId(@PathVariable("id") String id, @RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			Valoracion valoracion = valoracionService.obtenerPorId(id);
			return (valoracion != null) 
					? new ResponseEntity<>(valoracion, HttpStatus.OK) 
					: new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Guarda en la base de datos la valoración que envía el ciudadano al cerrar una incidencia.
	 */
	@PostMapping
	public ResponseEntity<String> guardar(@RequestBody Valoracion valoracion, @RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			valoracionService.guardar(valoracion);
			return new ResponseEntity<>("Valoración registrada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * ¡Ojo! Este método es solo para pruebas. Se mantiene público.
	 * POST /api/valoraciones/guardarLista
	 */
	@PostMapping("/guardarLista")
	public ResponseEntity<String> guardarLista(@RequestBody List<Valoracion> listaValoraciones) {
		try {
			for (Valoracion valoracion : listaValoraciones) {
				valoracionService.guardar(valoracion);
			}
			return new ResponseEntity<>("Carga masiva de valoraciones completada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Permite editar una valoración.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<String> modificar(
	        @PathVariable("id") String id, 
	        @RequestBody Valoracion valoracion, 
	        @RequestHeader("Authorization") String token) {
	    try {
	        String idToken = token.startsWith("Bearer ") ? token.substring(7) : token;
	        authService.verificarToken(idToken);
	        
	        valoracionService.modificar(valoracion);
	        
	        return new ResponseEntity<>("Valoración actualizada correctamente", HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	    }
	}

	/**
	 * Borra definitivamente una valoración del sistema usando su identificador.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") String id, @RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			valoracionService.eliminar(id);
			return new ResponseEntity<>("Valoración eliminada correctamente", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Actualiza el estado de la valoración sin borrarla físicamente.
	 */
	@PutMapping("/{id}/estado/{estado}")
	public ResponseEntity<String> cambiarEstado(@PathVariable String id, @PathVariable ModeloBase.Estados estado, @RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			valoracionService.cambiarEstado(id, estado);
			return new ResponseEntity<>("Estado de la valoración actualizado a: " + estado, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}