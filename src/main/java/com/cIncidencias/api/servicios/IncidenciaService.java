package com.cIncidencias.api.servicios;

import com.cIncidencias.api.modelos.Incidencia;
import com.cIncidencias.api.modelos.Incidencia.EstadosIncidencia;
import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.repositorios.IncidenciaRepository;
import com.cIncidencias.api.excepciones.NullParamsException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Servicio para gestionar la lógica de las incidencias ciudadanas.
 * Implementa IGenericoService para estandarizar la gestión de reportes urbanos.
 * Actúa como el núcleo de control para el flujo de trabajo desde que se reporta 
 * una avería hasta su resolución final.
 */
@Service
public class IncidenciaService implements IGenericoService<Incidencia> {

	private final IncidenciaRepository incidenciaRepository;

	public IncidenciaService(IncidenciaRepository incidenciaRepository) {
		this.incidenciaRepository = incidenciaRepository;
	}

	@Override
	public void guardar(Incidencia incidencia) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (incidencia == null || incidencia.getIdIncidencia() == null) {
			throw new NullParamsException("No se puede registrar una incidencia nula o sin ID.");
		}
		
		// Comprobamos si ya existe para evitar duplicados
		if (incidenciaRepository.existePorId(incidencia.getIdIncidencia())) {
			throw new NullParamsException("La incidencia con ID " + incidencia.getIdIncidencia() + " ya está registrada.");
		}

		if (incidencia.getTitulo() == null || incidencia.getTitulo().trim().isEmpty()) {
			throw new NullParamsException("El título de la incidencia es obligatorio.");
		}

		if (incidencia.getEstadoIncidencia() == null) {
			incidencia.setEstadoIncidencia(EstadosIncidencia.PENDIENTE);
		}
		
		incidenciaRepository.guardar(incidencia);
	}

	@Override
	public List<Incidencia> obtenerTodos() throws ExecutionException, InterruptedException {
		return incidenciaRepository.obtenerTodos();
	}

	@Override
	public Incidencia obtenerPorId(String idIncidencia) throws NullParamsException, ExecutionException, InterruptedException {
		if (idIncidencia == null || idIncidencia.trim().isEmpty()) {
			throw new NullParamsException("Se requiere un ID válido para buscar la incidencia.");
		}
		return incidenciaRepository.obtenerPorId(idIncidencia);
	}

	@Override
	public void eliminar(String idIncidencia) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (idIncidencia == null || idIncidencia.trim().isEmpty()) {
			throw new NullParamsException("Se requiere un ID para eliminar la incidencia.");
		}

		// Validamos existencia antes de borrar
		if (!incidenciaRepository.existePorId(idIncidencia)) {
			throw new NullParamsException("No se puede eliminar: la incidencia no existe.");
		}

		incidenciaRepository.eliminar(idIncidencia);
	}

	@Override
	public void modificar(Incidencia incidencia) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (incidencia == null || incidencia.getIdIncidencia() == null || incidencia.getIdIncidencia().trim().isEmpty()) {
			throw new NullParamsException("Datos insuficientes para modificar la incidencia.");
		}

		// Verificamos que la incidencia exista antes de intentar actualizarla
		if (!incidenciaRepository.existePorId(incidencia.getIdIncidencia())) {
			throw new NullParamsException("No se puede modificar una incidencia que no existe.");
		}

		incidenciaRepository.modificar(incidencia);
	}
	
	@Override
	public void cambiarEstado(String idIncidencia, ModeloBase.Estados estado) throws Exception {
		if (idIncidencia == null || idIncidencia.trim().isEmpty()) {
			throw new NullParamsException("Se necesita un ID válido para cambiar el estado de la incidencia.");
		}

		// Validación de existencia previa al cambio de estado
		if (!incidenciaRepository.existePorId(idIncidencia)) {
			throw new NullParamsException("No se encontró la incidencia para actualizar su estado.");
		}

		incidenciaRepository.cambiarEstado(idIncidencia, estado);
	}
}