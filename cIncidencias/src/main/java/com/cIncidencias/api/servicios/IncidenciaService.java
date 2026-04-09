package com.cIncidencias.api.servicios;

import com.cIncidencias.api.modelos.Incidencia;
import com.cIncidencias.api.repositorios.IncidenciaRepository;
import com.cIncidencias.api.excepciones.NullParamsException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Servicio para gestionar la lógica de las incidencias ciudadanas.
 * Implementa IGenericoService para estandarizar la gestión de reportes urbanos.
 */
@Service
public class IncidenciaService implements IGenericoService<Incidencia> {

	private final IncidenciaRepository incidenciaRepository;

	public IncidenciaService(IncidenciaRepository incidenciaRepository) {
		this.incidenciaRepository = incidenciaRepository;
	}

	/**
	 * Valida y registra una nueva incidencia.
	 * Asigna el estado 'PENDIENTE' por defecto si no se especifica uno.
	 * * @param incidencia Objeto con la información del reporte.
	 * @throws NullParamsException Si la incidencia es nula o carece de título/descripción.
	 * @throws ExecutionException Si falla la tarea asíncrona en Firestore.
	 * @throws InterruptedException Si se interrumpe la conexión con el servidor.
	 * @throws IOException Si ocurre un error al escribir en el log local.
	 */
	@Override
	public void guardar(Incidencia incidencia) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (incidencia == null) {
			throw new NullParamsException("No se puede registrar una incidencia nula.");
		}
		
		if (incidencia.getTitulo() == null || incidencia.getTitulo().trim().isEmpty()) {
			throw new NullParamsException("El título de la incidencia es obligatorio.");
		}

		// Lógica de negocio: establecer estado inicial si viene vacío
		if (incidencia.getEstado() == null || incidencia.getEstadoIncidencia().toString().isEmpty()) {
			incidencia.setEstadoIncidencia("PENDIENTE");
		}
		
		incidenciaRepository.guardar(incidencia);
	}

	/**
	 * Recupera todas las incidencias registradas en el sistema.
	 * * @return Lista de objetos Incidencia.
	 * @throws ExecutionException Si hay un error en la recuperación de datos desde Firestore.
	 * @throws InterruptedException Si se interrumpe el proceso de consulta.
	 */
	@Override
	public List<Incidencia> obtenerTodos() throws ExecutionException, InterruptedException {
		return incidenciaRepository.obtenerTodos();
	}

	/**
	 * Busca una incidencia específica mediante su identificador.
	 * * @param idIncidencia Identificador único del reporte.
	 * @return La Incidencia encontrada o null si no existe.
	 * @throws NullParamsException Si el identificador es nulo.
	 * @throws ExecutionException Si ocurre un fallo en la consulta al servidor.
	 * @throws InterruptedException Si se interrumpe la comunicación.
	 */
	@Override
	public Incidencia obtenerPorId(String idIncidencia) throws NullParamsException, ExecutionException, InterruptedException {
		if (idIncidencia == null) {
			throw new NullParamsException("Se requiere un ID válido para buscar la incidencia.");
		}
		return incidenciaRepository.obtenerPorId(idIncidencia);
	}

	/**
	 * Elimina una incidencia tras validar su identificador.
	 * * @param idIncidencia ID de la incidencia a borrar.
	 * @throws NullParamsException Si el ID es nulo.
	 * @throws ExecutionException Si el borrado falla en la base de datos.
	 * @throws InterruptedException Si se interrumpe la tarea.
	 * @throws IOException Si falla el registro de la acción en el archivo de log.
	 */
	@Override
	public void eliminar(String idIncidencia) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (idIncidencia == null) {
			throw new NullParamsException("Se requiere un ID para eliminar la incidencia.");
		}
		incidenciaRepository.eliminar(idIncidencia);
	}

	/**
	 * Actualiza los datos de una incidencia existente.
	 * * @param incidencia Objeto con los datos actualizados e ID válido.
	 * @throws NullParamsException Si la incidencia o su ID son nulos.
	 * @throws ExecutionException Si la actualización falla en Firestore.
	 * @throws InterruptedException Si se interrumpe la comunicación asíncrona.
	 * @throws IOException Si ocurre un error de escritura en el log.
	 */
	@Override
	public void modificar(Incidencia incidencia) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (incidencia == null || incidencia.getIdIncidencia() == null) {
			throw new NullParamsException("Datos insuficientes para modificar la incidencia.");
		}
		incidenciaRepository.modificar(incidencia);
	}
}