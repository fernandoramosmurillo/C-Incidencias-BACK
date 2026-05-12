package com.cIncidencias.api.controladores;

import com.cIncidencias.api.modelos.Incidencia;
import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.servicios.FirebaseAuthService;
import com.cIncidencias.api.servicios.IGenericoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de incidencias. Conecta las peticiones
 * externas con la lógica de negocio de IncidenciaService.
 */
@RestController
@RequestMapping("/api/incidencias")
@CrossOrigin(origins = "*")
public class IncidenciaController {

	private final IGenericoService<Incidencia> incidenciaService;
	private final FirebaseAuthService authService;

	/**
	 * Constructor para inyectar el servicio de incidencias y el de autenticación.
	 */
	public IncidenciaController(IGenericoService<Incidencia> incidenciaService, FirebaseAuthService authService) {
		this.incidenciaService = incidenciaService;
		this.authService = authService;
	}

	/**
	 * Devuelve la lista completa de todas las incidencias. GET /api/incidencias
	 */
	@GetMapping
	public ResponseEntity<List<Incidencia>> listar(@RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			List<Incidencia> lista = incidenciaService.obtenerTodos();
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Busca una incidencia concreta usando su ID único. GET /api/incidencias/{id}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Incidencia> obtenerPorId(@PathVariable("id") String id,
			@RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			Incidencia incidencia = incidenciaService.obtenerPorId(id);
			return (incidencia != null) ? new ResponseEntity<>(incidencia, HttpStatus.OK)
					: new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Registra una nueva incidencia en el sistema. POST /api/incidencias
	 */
	@PostMapping
	public ResponseEntity<String> guardar(@RequestBody Incidencia incidencia,
			@RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			incidenciaService.guardar(incidencia);
			return new ResponseEntity<>("Incidencia registrada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * ¡Ojo! Este método es solo para cargar datos de bulto. Se mantiene público
	 * según lo solicitado. POST /api/incidencias/guardarLista
	 */
	@PostMapping("/guardarLista")
	public ResponseEntity<String> guardarLista(@RequestBody List<Incidencia> listaIncidencias) {
		try {
			for (Incidencia incidencia : listaIncidencias) {
				incidenciaService.guardar(incidencia);
			}
			return new ResponseEntity<>("Carga masiva de incidencias completada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Permite modificar los datos de una incidencia que ya existe. PUT
	 * /api/incidencias
	 */
	@PutMapping("/{id}")
	public ResponseEntity<String> modificar(@PathVariable("id") String id, @RequestBody Incidencia incidencia,
			@RequestHeader("Authorization") String token) {
		try {
			String idToken = token.startsWith("Bearer ") ? token.substring(7) : token;
			authService.verificarToken(idToken);

			incidenciaService.modificar(incidencia);

			return new ResponseEntity<>("Incidencia actualizada correctamente", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Borra el registro de una incidencia por su ID. DELETE /api/incidencias/{id}
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") String id,
			@RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			incidenciaService.eliminar(id);
			return new ResponseEntity<>("Incidencia eliminada", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Actualiza el estado lógico (Activo, Eliminado, etc.). PUT
	 * /api/incidencias/{id}/estado/{estado}
	 */
	@PutMapping("/{id}/estado/{estado}")
	public ResponseEntity<String> cambiarEstado(@PathVariable String id, @PathVariable ModeloBase.Estados estado,
			@RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			incidenciaService.cambiarEstado(id, estado);
			return new ResponseEntity<>("Estado de la incidencia actualizado a: " + estado, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}