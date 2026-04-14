package com.cIncidencias.api.controladores;

import com.cIncidencias.api.modelos.Incidencia;
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
public class IncidenciaController {

	private final IGenericoService<Incidencia> incidenciaService;

	public IncidenciaController(IGenericoService<Incidencia> incidenciaService) {
		this.incidenciaService = incidenciaService;
	}

	/**
	 * Obtiene el listado de todas las incidencias reportadas.
	 * GET /api/incidencias
	 */
	@GetMapping
	public ResponseEntity<List<Incidencia>> listar() {
		try {
			List<Incidencia> lista = incidenciaService.obtenerTodos();
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Busca una incidencia por su ID.
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
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Crea una nueva incidencia
	 * POST /api/incidencias
	 */
	@PostMapping
	public ResponseEntity<String> guardar(@RequestBody Incidencia incidencia) {
		try {
			incidenciaService.guardar(incidencia);
			return new ResponseEntity<>("Incidencia registrada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	
	/**
	 * ¡Advertencia!
	 * Este metodo solo debe usarse durante las pruebas y desarollo
	 * 
	 * Crea una nueva incidencia
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
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	/**
	 * Actualiza los datos de una incidencia existente (ej. cambiar el estado).
	 * PUT /api/incidencias
	 */
	@PutMapping
	public ResponseEntity<String> modificar(@RequestBody Incidencia incidencia) {
		try {
			incidenciaService.modificar(incidencia);
			return new ResponseEntity<>("Incidencia actualizada correctamente", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Elimina una incidencia
	 * DELETE /api/incidencias/{id}
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") String id) {
		try {
			incidenciaService.eliminar(id);
			return new ResponseEntity<>("Incidencia eliminada", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PutMapping("/estado/eliminarTemporalmente/{id}")
	public ResponseEntity<String> eliminarTemporalmente(@PathVariable String id) {
	    try {
	        incidenciaService.eliminarTemporalmente(id);
	        return new ResponseEntity<>("Incidencia marcada como ELIMINADA", HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
}