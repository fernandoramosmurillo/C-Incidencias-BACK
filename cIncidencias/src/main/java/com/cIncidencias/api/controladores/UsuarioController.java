package com.cIncidencias.api.controladores;

import com.cIncidencias.api.modelos.Usuario;
import com.cIncidencias.api.servicios.IGenericoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de usuarios.
 * Permite administrar los perfiles de ciudadanos y administradores del sistema.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	private final IGenericoService<Usuario> usuarioService;

	public UsuarioController(IGenericoService<Usuario> usuarioService) {
		this.usuarioService = usuarioService;
	}

	/**
	 * Obtiene el listado de todos los usuarios registrados.
	 * GET /api/usuarios
	 */
	@GetMapping
	public ResponseEntity<List<Usuario>> listar() {
		try {
			List<Usuario> lista = usuarioService.obtenerTodos();
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Busca un usuario por su identificador único.
	 * GET /api/usuarios/{id}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> obtenerPorId(@PathVariable("id") String id) {
		try {
			Usuario usuario = usuarioService.obtenerPorId(id);
			return (usuario != null) 
					? new ResponseEntity<>(usuario, HttpStatus.OK) 
					: new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Registra un nuevo usuario en el sistema.
	 * POST /api/usuarios
	 */
	@PostMapping
	public ResponseEntity<String> guardar(@RequestBody Usuario usuario) {
		try {
			usuarioService.guardar(usuario);
			return new ResponseEntity<>("Usuario registrado con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * ¡Advertencia!
	 * Este método solo debe usarse durante las pruebas y desarrollo.
	 * * Crea una lista de nuevos usuarios de forma masiva.
	 * POST /api/usuarios/guardarLista
	 */
	@PostMapping("/guardarLista")
	public ResponseEntity<String> guardarLista(@RequestBody List<Usuario> listaUsuarios) {
		try {
			for (Usuario usuario : listaUsuarios) {
				usuarioService.guardar(usuario);
			}
			return new ResponseEntity<>("Carga masiva de usuarios completada con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Actualiza la información de un usuario existente.
	 * PUT /api/usuarios
	 */
	@PutMapping
	public ResponseEntity<String> modificar(@RequestBody Usuario usuario) {
		try {
			usuarioService.modificar(usuario);
			return new ResponseEntity<>("Perfil de usuario actualizado correctamente", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Elimina a un usuario del sistema por su ID.
	 * DELETE /api/usuarios/{id}
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") String id) {
		try {
			usuarioService.eliminar(id);
			return new ResponseEntity<>("Usuario eliminado correctamente", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/estado/eliminarTemporalmente/{id}")
	public ResponseEntity<String> eliminarTemporalmente(@PathVariable String id) {
	    try {
	        usuarioService.eliminarTemporalmente(id);
	        return new ResponseEntity<>("Usuario marcado como ELIMINADO", HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
}