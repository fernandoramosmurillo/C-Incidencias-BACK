package com.cIncidencias.api.controladores;

import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.modelos.Noticia;
import com.cIncidencias.api.servicios.FirebaseAuthService;
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
	private final FirebaseAuthService authService;

	/**
	 * Constructor para que Spring nos inyecte el servicio de noticias y el de autenticación.
	 */
	public NoticiaController(IGenericoService<Noticia> noticiaService, FirebaseAuthService authService) {
		this.noticiaService = noticiaService;
		this.authService = authService;
	}

	/**
	 * Trae todas las noticias de la base de datos para mostrarlas en el tablón de anuncios.
	 */
	@GetMapping
	public ResponseEntity<List<Noticia>> listar(@RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
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
	public ResponseEntity<Noticia> obtenerPorId(@PathVariable("id") String id, @RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
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
	public ResponseEntity<String> guardar(@RequestBody Noticia noticia, @RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			noticiaService.guardar(noticia);
			return new ResponseEntity<>("Noticia publicada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * ¡Advertencia! Este método se mantiene público para carga masiva en desarrollo.
	 * POST /api/noticias/guardarLista
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
	@PutMapping("/{id}")
	public ResponseEntity<String> modificar(
	        @PathVariable("id") String id, 
	        @RequestBody Noticia noticia, 
	        @RequestHeader("Authorization") String token) {
	    try {
	        String idToken = token.startsWith("Bearer ") ? token.substring(7) : token;
	        authService.verificarToken(idToken);
	        
	        noticiaService.modificar(noticia);
	        
	        return new ResponseEntity<>("Noticia actualizada correctamente", HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	    }
	}

	/**
	 * Elimina definitivamente una noticia de la colección.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") String id, @RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
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
	public ResponseEntity<String> cambiarEstado(@PathVariable String id, @PathVariable ModeloBase.Estados estado, @RequestHeader("Authorization") String token) {
		try {
			authService.verificarToken(token);
			noticiaService.cambiarEstado(id, estado);
			return new ResponseEntity<>("Estado de la noticia actualizado a: " + estado, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}