package com.cIncidencias.api.controladores;

import com.cIncidencias.api.modelos.Incidencia;
import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.servicios.IGenericoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de incidencias.
 * Conecta las peticiones externas con la lógica de negocio de IncidenciaService.
 */
@RestController
@RequestMapping("/api/incidencias")
@CrossOrigin(origins = "*")
public class IncidenciaController {

	private final IGenericoService<Incidencia> incidenciaService;

	/**
	 * Constructor para inyectar el servicio de incidencias.
	 */
	public IncidenciaController(IGenericoService<Incidencia> incidenciaService) {
		this.incidenciaService = incidenciaService;
	}

	/**
	 * Devuelve la lista completa de todas las incidencias que hay en la base de datos.
	 * GET /api/incidencias
	 */
	@GetMapping
	public ResponseEntity<List<Incidencia>> listar() {
		try {
			List<Incidencia> lista = incidenciaService.obtenerTodos();
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Busca una incidencia concreta usando su ID único.
	 * GET /api/incidencias/{id}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Incidencia> obtenerPorId(@PathVariable("id") String id) {
		try {
			Incidencia incidencia = incidenciaService.obtenerPorId(id);
			return (incidencia != null) 
					? new ResponseEntity<>(incidencia, HttpStatus.OK) 
					: new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Registra una nueva incidencia en el sistema.
	 * POST /api/incidencias
	 */
	@PostMapping
	public ResponseEntity<String> guardar(@RequestBody Incidencia incidencia) {
		try {
			incidenciaService.guardar(incidencia);
			return new ResponseEntity<>("Incidencia registrada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	
	/**
	 * ¡Ojo! Este método es solo para cargar datos de bulto en pruebas y desarrollo.
	 * POST /api/incidencias/guardarLista
	 */
	@PostMapping("/guardarLista")
	public ResponseEntity<String> guardarLista(@RequestBody List<Incidencia> listaIncidencias){
		try {
			for (Incidencia incidencia : listaIncidencias) {
				incidenciaService.guardar(incidencia);
			}
			return new ResponseEntity<>("Incidencia registrada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	/**
	 * Permite modificar los datos de una incidencia que ya existe.
	 * PUT /api/incidencias
	 */
	@PutMapping
	public ResponseEntity<String> modificar(@RequestBody Incidencia incidencia) {
		try {
			incidenciaService.modificar(incidencia);
			return new ResponseEntity<>("Incidencia actualizada correctamente", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Borra el registro de una incidencia por su ID.
	 * DELETE /api/incidencias/{id}
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") String id) {
		try {
			incidenciaService.eliminar(id);
			return new ResponseEntity<>("Incidencia eliminada", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/**
	 * Actualiza el estado lógico (Activo, Eliminado, etc.) sin borrar los datos del servidor.
	 * PUT /api/incidencias/{id}/estado/{estado}
	 */
	@PutMapping("/{id}/estado/{estado}")
	public ResponseEntity<String> cambiarEstado(@PathVariable String id, @PathVariable ModeloBase.Estados estado) {
	    try {
	        incidenciaService.cambiarEstado(id, estado);
	        return new ResponseEntity<>("Estado de la incidencia actualizado a: " + estado, HttpStatus.OK);
	    } catch (Exception e) {
	    	e.printStackTrace();
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
}