package com.cIncidencias.api.servicios;

import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.modelos.Notificacion;
import com.cIncidencias.api.repositorios.NotificacionRepository;
import com.cIncidencias.api.excepciones.NullParamsException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Servicio para gestionar la lógica de las notificaciones y alertas del sistema.
 * Implementa IGenericoService para asegurar que los avisos a usuarios sean consistentes.
 * Se encarga de validar que cada alerta llegue al destinatario correcto y cumpla 
 * con los requisitos de contenido antes de disparar el aviso.
 */
@Service
public class NotificacionService implements IGenericoService<Notificacion> {

	private final NotificacionRepository notificacionRepository;

	public NotificacionService(NotificacionRepository notificacionRepository) {
		this.notificacionRepository = notificacionRepository;
	}

	@Override
	public void guardar(Notificacion notificacion) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (notificacion == null || notificacion.getIdNotificacion() == null) {
			throw new NullParamsException("No se puede procesar una notificación nula o sin ID.");
		}

		// Verificación de duplicados: No permitimos registrar dos veces la misma notificación
		if (notificacionRepository.existePorId(notificacion.getIdNotificacion())) {
			throw new NullParamsException("La notificación con ID " + notificacion.getIdNotificacion() + " ya existe.");
		}

		if (notificacion.getMensaje() == null || notificacion.getMensaje().trim().isEmpty()) {
			throw new NullParamsException("El mensaje de la notificación es obligatorio.");
		}
		
		if (notificacion.getIdDestinatarios() == null || notificacion.getIdDestinatarios().isEmpty()) {
			throw new NullParamsException("La notificación debe tener al menos un usuario de destino.");
		}

		notificacionRepository.guardar(notificacion);
	}

	@Override
	public List<Notificacion> obtenerTodos() throws ExecutionException, InterruptedException {
		return notificacionRepository.obtenerTodos();
	}

	@Override
	public Notificacion obtenerPorId(String idNotificacion) throws NullParamsException, ExecutionException, InterruptedException {
		if (idNotificacion == null || idNotificacion.trim().isEmpty()) {
			throw new NullParamsException("Se requiere un ID válido para buscar la notificación.");
		}
		return notificacionRepository.obtenerPorId(idNotificacion);
	}

	@Override
	public void eliminar(String idNotificacion) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (idNotificacion == null || idNotificacion.trim().isEmpty()) {
			throw new NullParamsException("Es necesario un ID válido para eliminar la notificación.");
		}

		// Validación de existencia previa al borrado físico
		if (!notificacionRepository.existePorId(idNotificacion)) {
			throw new NullParamsException("No se puede eliminar: la notificación no existe en el sistema.");
		}

		notificacionRepository.eliminar(idNotificacion);
	}

	@Override
	public void modificar(Notificacion notificacion) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (notificacion == null || notificacion.getIdNotificacion() == null || notificacion.getIdNotificacion().trim().isEmpty()) {
			throw new NullParamsException("Datos insuficientes para modificar la notificación (ID ausente).");
		}

		// Aseguramos que la notificación que se pretende editar existe realmente
		if (!notificacionRepository.existePorId(notificacion.getIdNotificacion())) {
			throw new NullParamsException("Imposible modificar: la notificación no existe.");
		}

		notificacionRepository.modificar(notificacion);
	}
	
	@Override
	public void cambiarEstado(String idNotificacion, ModeloBase.Estados estado) throws Exception {
		if (idNotificacion == null || idNotificacion.trim().isEmpty()) {
			throw new NullParamsException("Se necesita un ID válido para cambiar el estado de la notificación.");
		}

		// Verificamos existencia antes de la actualización parcial
		if (!notificacionRepository.existePorId(idNotificacion)) {
			throw new NullParamsException("No se encontró la notificación para actualizar su estado.");
		}

		notificacionRepository.cambiarEstado(idNotificacion, estado);
	}
}