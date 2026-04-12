package com.cIncidencias.proyecto.servicios;

import com.cIncidencias.api.excepciones.NullParamsException;
import com.cIncidencias.api.modelos.Comentario;
import com.cIncidencias.api.repositorios.ComentarioRepository;
import com.cIncidencias.api.servicios.ComentarioService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComentarioServiceTest {

	@Mock
	private ComentarioRepository comentarioRepository;

	@InjectMocks
	private ComentarioService comentarioService;

	@Nested
	@DisplayName("Tests para el método Guardar")
	class GuardarTests {

		@Test
		@DisplayName("Validación de errores al guardar")
		void validacionGuardado() {
			Comentario vacio = new Comentario();
			vacio.setTexto("   ");

			assertAll("Errores de guardado", () -> {
				NullParamsException ex = assertThrows(NullParamsException.class, () -> comentarioService.guardar(null));
				assertEquals("Se necesita un comentario para publicarlo", ex.getMessage());
			}, () -> {
				NullParamsException ex = assertThrows(NullParamsException.class,
						() -> comentarioService.guardar(vacio));
				assertEquals("El texto del comentario no puede estar vacío.", ex.getMessage());
			});

			// Comprueba si no se ha interactuado con el comentarioRepository
			verifyNoInteractions(comentarioRepository);
		}

		@Test
		@DisplayName("Guardado correcto")
		void guardarOk() throws Exception {
			Comentario c = new Comentario();
			c.setIdComentario("COM-1");
			c.setTexto("La reparación quedó perfecta.");
			comentarioService.guardar(c);
			verify(comentarioRepository).guardar(c);
		}
	}

	@Nested
	@DisplayName("Tests para Búsqueda")
	class BusquedaTests {

		@Test
		@DisplayName("Error al buscar con ID nulo o vacío")
		void buscarIdInvalido() {
			assertAll("Errores de búsqueda", () -> {
				NullParamsException ex = assertThrows(NullParamsException.class,
						() -> comentarioService.obtenerPorId(null));
				assertEquals("Es necesario un ID válido para buscar el comentario.", ex.getMessage());
			}, () -> {
				NullParamsException ex = assertThrows(NullParamsException.class,
						() -> comentarioService.obtenerPorId("   "));
				assertEquals("Es necesario un ID válido para buscar el comentario.", ex.getMessage());
			});

			// Comprueba si no se ha interactuado con el comentarioRepository
			verifyNoInteractions(comentarioRepository);
		}

		@Test
		@DisplayName("Búsqueda correcta")
		void buscarOk() throws Exception {
			String id = "COM-100";
			comentarioService.obtenerPorId(id);
			
			// Comprueba si no se ha interactuado con el comentarioRepository
			verify(comentarioRepository).obtenerPorId(id);
		}
	}

	@Nested
	@DisplayName("Tests para Eliminación")
	class EliminacionTests {

		@Test
		@DisplayName("Error al eliminar con ID nulo o vacío")
		void eliminarIdInvalido() {
			assertAll("Errores de eliminación", () -> {
				NullParamsException ex = assertThrows(NullParamsException.class,
						() -> comentarioService.eliminar(null));
				assertEquals("Es necesario un ID válido para eliminar el comentario.", ex.getMessage());
			}, () -> {
				NullParamsException ex = assertThrows(NullParamsException.class, () -> comentarioService.eliminar(""));
				assertEquals("Es necesario un ID válido para eliminar el comentario.", ex.getMessage());
			});

			// Comprueba si no se ha interactuado con el comentarioRepository
			verifyNoInteractions(comentarioRepository);
		}

		@Test
		@DisplayName("Eliminación correcta")
		void eliminarOk() throws Exception {
			String id = "COM-100";
			comentarioService.eliminar(id);
			verify(comentarioRepository).eliminar(id);
		}
	}

	@Nested
	@DisplayName("Tests para Modificación")
	class ModificarTests {

		@Test
		@DisplayName("Validación de errores al modificar")
		void modificarError() {
			Comentario sinId = new Comentario();
			sinId.setTexto("Texto");

			Comentario textoVacio = new Comentario();
			textoVacio.setIdComentario("ID-1");
			textoVacio.setTexto("  ");

			assertAll("Errores de modificación", () -> {
				NullParamsException ex = assertThrows(NullParamsException.class,
						() -> comentarioService.modificar(null));
				assertEquals("Datos insuficientes para modificar el comentario (ID ausente).", ex.getMessage());
			}, () -> {
				NullParamsException ex = assertThrows(NullParamsException.class,
						() -> comentarioService.modificar(sinId));
				assertEquals("Datos insuficientes para modificar el comentario (ID ausente).", ex.getMessage());
			}, () -> {
				NullParamsException ex = assertThrows(NullParamsException.class,
						() -> comentarioService.modificar(textoVacio));
				assertEquals("El nuevo texto del comentario no puede estar vacío.", ex.getMessage());
			});
			
			// Comprueba si no se ha interactuado con el comentarioRepository
			verifyNoInteractions(comentarioRepository);
		}

		@Test
		@DisplayName("Modificación correcta")
		void modificarOk() throws Exception {
			Comentario c = new Comentario();
			c.setIdComentario("ID-123");
			c.setTexto("Texto modificado");
			comentarioService.modificar(c);
			verify(comentarioRepository).modificar(c);
		}
	}
}