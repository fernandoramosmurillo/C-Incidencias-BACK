package com.cIncidencias.api.servicios;

import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.modelos.Valoracion;
import com.cIncidencias.api.repositorios.ValoracionRepository;
import com.cIncidencias.api.excepciones.NullParamsException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Servicio para gestionar la lógica de las valoraciones de incidencias.
 * Implementa IGenericoService para centralizar la validación del feedback de los ciudadanos.
 * Garantiza que las métricas de satisfacción cumplan con las reglas de negocio establecidas.
 */
@Service
public class ValoracionService implements IGenericoService<Valoracion> {

	private final ValoracionRepository valoracionRepository;

	public ValoracionService(ValoracionRepository valoracionRepository) {
		this.valoracionRepository = valoracionRepository;
	}

	/**
	 * Valida y registra una nueva valoración en el sistema.
	 * Verifica que la puntuación esté dentro del rango lógico de 1 a 5 estrellas.
	 * @param valoracion Objeto Valoracion con el feedback del usuario.
	 * @throws NullParamsException Si el objeto es nulo o la puntuación es inválida.
	 * @throws ExecutionException Si ocurre un error en la persistencia en Firestore.
	 * @throws InterruptedException Si se interrumpe la conexión con el servidor.
	 * @throws IOException Si falla el registro en el log local.
	 */
	@Override
	public void guardar(Valoracion valoracion) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (valoracion == null) {
			throw new NullParamsException("No se puede registrar una valoración nula.");
		}

		// Lógica de negocio: Validar que la puntuación sea coherente
		if (valoracion.getPuntuacion() < 1 || valoracion.getPuntuacion() > 5) {
			throw new NullParamsException("La puntuación debe estar comprendida entre 1 y 5.");
		}

		valoracionRepository.guardar(valoracion);
	}

	/**
	 * Obtiene todas las valoraciones registradas en el sistema.
	 * @return Lista de objetos Valoracion.
	 * @throws ExecutionException Si hay un fallo al recuperar datos de Firestore.
	 * @throws InterruptedException Si se interrumpe el proceso de consulta.
	 */
	@Override
	public List<Valoracion> obtenerTodos() throws ExecutionException, InterruptedException {
		return valoracionRepository.obtenerTodos();
	}

	/**
	 * Busca una valoración específica mediante su identificador único.
	 * @param idValoracion Identificador del documento de valoración.
	 * @return La Valoracion encontrada o null si no existe.
	 * @throws NullParamsException Si el ID proporcionado es nulo o está vacío.
	 * @throws ExecutionException Si la consulta falla en el servidor.
	 * @throws InterruptedException Si se corta la comunicación asíncrona.
	 */
	@Override
	public Valoracion obtenerPorId(String idValoracion) throws NullParamsException, ExecutionException, InterruptedException {
		if (idValoracion == null || idValoracion.trim().isEmpty()) {
			throw new NullParamsException("Se requiere un ID válido para buscar la valoración.");
		}
		return valoracionRepository.obtenerPorId(idValoracion);
	}

	/**
	 * Elimina permanentemente una valoración del sistema tras validar su ID.
	 * @param idValoracion ID de la valoración a borrar.
	 * @throws NullParamsException Si el ID es nulo o está vacío.
	 * @throws ExecutionException Si el borrado falla en la base de datos.
	 * @throws InterruptedException Si el hilo es interrumpido.
	 * @throws IOException Si ocurre un error al registrar la acción en el log.
	 */
	@Override
	public void eliminar(String idValoracion) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (idValoracion == null || idValoracion.trim().isEmpty()) {
			throw new NullParamsException("Es necesario un ID válido para eliminar la valoración.");
		}
		valoracionRepository.eliminar(idValoracion);
	}

	/**
	 * Actualiza la información de una valoración ya existente.
	 * @param valoracion Objeto con los datos actualizados e ID válido.
	 * @throws NullParamsException Si el objeto o su ID son nulos o vacíos.
	 * @throws ExecutionException Si la actualización falla en Firestore.
	 * @throws InterruptedException Si se interrumpe la tarea asíncrona.
	 * @throws IOException Si falla la escritura en el log de incidencias.
	 */
	@Override
	public void modificar(Valoracion valoracion) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (valoracion == null || valoracion.getIdValoracion() == null || valoracion.getIdValoracion().trim().isEmpty()) {
			throw new NullParamsException("Datos insuficientes para modificar la valoración (ID ausente).");
		}
		
		// Validar de nuevo el rango de puntuación en caso de edición
		if (valoracion.getPuntuacion() < 1 || valoracion.getPuntuacion() > 5) {
			throw new NullParamsException("La nueva puntuación debe estar comprendida entre 1 y 5.");
		}

		valoracionRepository.modificar(valoracion);
	}
	
	/**
	 * Gestiona el cambio de estado de la valoración (ej: para moderar comentarios ofensivos 
	 * o realizar un borrado lógico).
	 */
	@Override
	public void cambiarEstado(String idValoracion, ModeloBase.Estados estado) throws Exception {

	    if (idValoracion == null || idValoracion.trim().isEmpty()) {
	        throw new NullParamsException("Se necesita un ID válido para cambiar el estado de la valoración.");
	    }

	    valoracionRepository.cambiarEstado(idValoracion, estado);
	}
}