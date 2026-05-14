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

	@Override
	public void guardar(Valoracion valoracion) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (valoracion == null || valoracion.getIdValoracion() == null) {
			throw new NullParamsException("No se puede registrar una valoración nula o sin ID.");
		}

		// Verificación de duplicados
		if (valoracionRepository.existePorId(valoracion.getIdValoracion())) {
			throw new NullParamsException("La valoración con ID " + valoracion.getIdValoracion() + " ya existe.");
		}

		// Lógica de negocio: Validar que la puntuación sea coherente
		if (valoracion.getPuntuacion() < 1 || valoracion.getPuntuacion() > 5) {
			throw new NullParamsException("La puntuación debe estar comprendida entre 1 y 5.");
		}

		valoracionRepository.guardar(valoracion);
	}

	@Override
	public List<Valoracion> obtenerTodos() throws ExecutionException, InterruptedException {
		return valoracionRepository.obtenerTodos();
	}

	@Override
	public Valoracion obtenerPorId(String idValoracion) throws NullParamsException, ExecutionException, InterruptedException {
		if (idValoracion == null || idValoracion.trim().isEmpty()) {
			throw new NullParamsException("Se requiere un ID válido para buscar la valoración.");
		}
		return valoracionRepository.obtenerPorId(idValoracion);
	}

	@Override
	public void eliminar(String idValoracion) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (idValoracion == null || idValoracion.trim().isEmpty()) {
			throw new NullParamsException("Es necesario un ID válido para eliminar la valoración.");
		}

		// Validación de existencia previa al borrado
		if (!valoracionRepository.existePorId(idValoracion)) {
			throw new NullParamsException("No se puede eliminar: la valoración no existe.");
		}

		valoracionRepository.eliminar(idValoracion);
	}

	@Override
	public void modificar(Valoracion valoracion) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (valoracion == null || valoracion.getIdValoracion() == null || valoracion.getIdValoracion().trim().isEmpty()) {
			throw new NullParamsException("Datos insuficientes para modificar la valoración (ID ausente).");
		}

		// Verificamos que la valoración exista antes de intentar actualizarla
		if (!valoracionRepository.existePorId(valoracion.getIdValoracion())) {
			throw new NullParamsException("No se puede modificar una valoración que no existe en el sistema.");
		}
		
		if (valoracion.getPuntuacion() < 1 || valoracion.getPuntuacion() > 5) {
			throw new NullParamsException("La nueva puntuación debe estar comprendida entre 1 y 5.");
		}

		valoracionRepository.modificar(valoracion);
	}
	
	@Override
	public void cambiarEstado(String idValoracion, ModeloBase.Estados estado) throws Exception {
		if (idValoracion == null || idValoracion.trim().isEmpty()) {
			throw new NullParamsException("Se necesita un ID válido para cambiar el estado de la valoración.");
		}

		// Validación de existencia previa al cambio de estado
		if (!valoracionRepository.existePorId(idValoracion)) {
			throw new NullParamsException("No se encontró la valoración para actualizar su estado.");
		}

		valoracionRepository.cambiarEstado(idValoracion, estado);
	}
}