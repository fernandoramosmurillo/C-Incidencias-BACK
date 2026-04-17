package com.cIncidencias.api.controladores;

import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.modelos.Valoracion;
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

	public ValoracionController(IGenericoService<Valoracion> valoracionService) {
		this.valoracionService = valoracionService;
	}

	/**
	 * Obtiene el listado de todas las valoraciones enviadas.
	 * GET /api/valoraciones
	 */
	@GetMapping
	public ResponseEntity<List<Valoracion>> listar() {
		try {
			List<Valoracion> lista = valoracionService.obtenerTodos();
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Busca una valoración específica por su ID.
	 * GET /api/valoraciones/{id}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Valoracion> obtenerPorId(@PathVariable("id") String id) {
		try {
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
	 * Registra una nueva valoración de un usuario.
	 * POST /api/valoraciones
	 */
	@PostMapping
	public ResponseEntity<String> guardar(@RequestBody Valoracion valoracion) {
		try {
			valoracionService.guardar(valoracion);
			return new ResponseEntity<>("Valoración registrada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * ¡Advertencia!
	 * Este método solo debe usarse durante las pruebas y desarrollo.
	 * * Registra una lista de valoraciones de forma masiva.
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
	 * Modifica una valoración existente (ej. corregir el comentario).
	 * PUT /api/valoraciones
	 */
	@PutMapping
	public ResponseEntity<String> modificar(@RequestBody Valoracion valoracion) {
		try {
			valoracionService.modificar(valoracion);
			return new ResponseEntity<>("Valoración actualizada correctamente", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Elimina una valoración por su ID.
	 * DELETE /api/valoraciones/{id}
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") String id) {
		try {
			valoracionService.eliminar(id);
			return new ResponseEntity<>("Valoración eliminada correctamente", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Cambia el estado de la valoración (ACTIVO, INACTIVO, ELIMINADO, etc.)
	 * PUT /api/valoraciones/{id}/estado/{estado}
	 */
	@PutMapping("/{id}/estado/{estado}")
	public ResponseEntity<String> cambiarEstado(@PathVariable String id, @PathVariable ModeloBase.Estados estado) {
	    try {
	        valoracionService.cambiarEstado(id, estado);
	        return new ResponseEntity<>("Estado de la valoración actualizado a: " + estado, HttpStatus.OK);
	    } catch (Exception e) {
	    	e.printStackTrace();
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
}