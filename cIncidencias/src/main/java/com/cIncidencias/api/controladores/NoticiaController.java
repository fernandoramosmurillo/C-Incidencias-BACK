package com.cIncidencias.api.controladores;

import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.modelos.Noticia;
import com.cIncidencias.api.servicios.IGenericoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de noticias y comunicados oficiales.
 * Conecta las peticiones externas con la lógica de negocio de NoticiaService.
 */
@RestController
@RequestMapping("/api/noticias")
@CrossOrigin(origins = "*")
public class NoticiaController {

	private final IGenericoService<Noticia> noticiaService;

	/**
	 * Constructor para que Spring nos inyecte el servicio de noticias.
	 */
	public NoticiaController(IGenericoService<Noticia> noticiaService) {
		this.noticiaService = noticiaService;
	}

	/**
	 * Trae todas las noticias de la base de datos para mostrarlas en el tablón de anuncios.
	 */
	@GetMapping
	public ResponseEntity<List<Noticia>> listar() {
		try {
			List<Noticia> lista = noticiaService.obtenerTodos();
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Busca una noticia concreta por su ID. Útil para cuando el usuario pulsa en "Leer más".
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Noticia> obtenerPorId(@PathVariable("id") String id) {
		try {
			Noticia noticia = noticiaService.obtenerPorId(id);
			return (noticia != null) 
					? new ResponseEntity<>(noticia, HttpStatus.OK) 
					: new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Guarda una noticia nueva enviada desde el panel de administración.
	 */
	@PostMapping
	public ResponseEntity<String> guardar(@RequestBody Noticia noticia) {
		try {
			noticiaService.guardar(noticia);
			return new ResponseEntity<>("Noticia publicada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * ¡Advertencia! Este método lo uso solo para meter noticias de ejemplo 
	 * de golpe durante el desarrollo.
	 */
	@PostMapping("/guardarLista")
	public ResponseEntity<String> guardarLista(@RequestBody List<Noticia> listaNoticias) {
		try {
			for (Noticia noticia : listaNoticias) {
				noticiaService.guardar(noticia);
			}
			return new ResponseEntity<>("Carga masiva de noticias registrada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Modifica los datos de una noticia que ya esté publicada.
	 */
	@PutMapping
	public ResponseEntity<String> modificar(@RequestBody Noticia noticia) {
		try {
			noticiaService.modificar(noticia);
			return new ResponseEntity<>("Noticia actualizada correctamente", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Elimina definitivamente una noticia de la colección.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") String id) {
		try {
			noticiaService.eliminar(id);
			return new ResponseEntity<>("Noticia eliminada correctamente", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Cambia el estado de la noticia. Sirve para archivar noticias o 
	 * borrarlas de forma lógica sin eliminarlas del servidor.
	 */
	@PutMapping("/{id}/estado/{estado}")
	public ResponseEntity<String> cambiarEstado(@PathVariable String id, @PathVariable ModeloBase.Estados estado) {
	    try {
	        noticiaService.cambiarEstado(id, estado);
	        return new ResponseEntity<>("Estado de la noticia actualizado a: " + estado, HttpStatus.OK);
	    } catch (Exception e) {
	    	e.printStackTrace();
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
}