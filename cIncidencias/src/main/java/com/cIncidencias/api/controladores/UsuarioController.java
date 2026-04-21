package com.cIncidencias.api.controladores;

import com.cIncidencias.api.modelos.ModeloBase;
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
@CrossOrigin(origins = "*")
public class UsuarioController {

	private final IGenericoService<Usuario> usuarioService;

	/**
	 * Inyectamos el servicio para gestionar toda la lógica de los usuarios.
	 */
	public UsuarioController(IGenericoService<Usuario> usuarioService) {
		this.usuarioService = usuarioService;
	}

	/**
	 * Devuelve la lista de todos los usuarios que tenemos en la base de datos.
	 * GET /api/usuarios
	 */
	@GetMapping
	public ResponseEntity<List<Usuario>> listar() {
		try {
			List<Usuario> lista = usuarioService.obtenerTodos();
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Busca los datos de un usuario concreto a través de su ID único.
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
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Da de alta a un nuevo usuario en el sistema.
	 * POST /api/usuarios
	 */
	@PostMapping
	public ResponseEntity<String> guardar(@RequestBody Usuario usuario) {
		try {
			usuarioService.guardar(usuario);
			return new ResponseEntity<>("Usuario registrado con éxito", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Actualiza la información (nombre, apellidos, etc.) de un perfil que ya existe.
	 * PUT /api/usuarios
	 */
	@PutMapping
	public ResponseEntity<String> modificar(@RequestBody Usuario usuario) {
		try {
			usuarioService.modificar(usuario);
			return new ResponseEntity<>("Perfil de usuario actualizado correctamente", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Elimina por completo a un usuario del sistema usando su ID.
	 * DELETE /api/usuarios/{id}
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> eliminar(@PathVariable("id") String id) {
		try {
			usuarioService.eliminar(id);
			return new ResponseEntity<>("Usuario eliminado correctamente", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Cambia el estado del perfil sin necesidad de borrar los datos. 
	 * Útil para bloqueos temporales o bajas lógicas.
	 */
	@PutMapping("/{id}/estado/{estado}")
	public ResponseEntity<String> cambiarEstado(@PathVariable String id, @PathVariable ModeloBase.Estados estado) {
	    try {
	        usuarioService.cambiarEstado(id, estado);
	        return new ResponseEntity<>("Estado del usuario actualizado a: " + estado, HttpStatus.OK);
	    } catch (Exception e) {
	    	e.printStackTrace();
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
}