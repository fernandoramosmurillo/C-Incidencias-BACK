package com.cIncidencias.api.servicios;

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
 */
@Service
public class NotificacionService implements IGenericoService<Notificacion> {

	private final NotificacionRepository notificacionRepository;

	public NotificacionService(NotificacionRepository notificacionRepository) {
		this.notificacionRepository = notificacionRepository;
	}

	/**
	 * Valida y registra una nueva notificación.
	 * Verifica que el mensaje y el ID del destinatario no estén vacíos.
	 * * @param notificacion Objeto Notificacion a guardar.
	 * @throws NullParamsException Si el objeto es nulo o faltan datos del mensaje/usuario.
	 * @throws ExecutionException Si ocurre un error en la tarea asíncrona de Firestore.
	 * @throws InterruptedException Si se interrumpe el hilo durante la comunicación.
	 * @throws IOException Si falla el registro en el log local.
	 */
	@Override
	public void guardar(Notificacion notificacion) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (notificacion == null) {
			throw new NullParamsException("No se puede procesar una notificación nula.");
		}

		if (notificacion.getMensaje() == null || notificacion.getMensaje().trim().isEmpty()) {
			throw new NullParamsException("El mensaje de la notificación es obligatorio.");
		}
		
		// Se verifica que tiene un usuario de destino
		if (notificacion.getIdDestinatario() == null || notificacion.getIdDestinatario().size() < 1) {
			throw new NullParamsException("La notificación debe tener un usuario de destino.");
		}

		notificacionRepository.guardar(notificacion);
	}

	/**
	 * Recupera el historial de todas las notificaciones del sistema.
	 * * @return Lista de objetos Notificacion.
	 * @throws ExecutionException Si hay un fallo al obtener datos del servidor.
	 * @throws InterruptedException Si se corta la conexión con Firestore.
	 */
	@Override
	public List<Notificacion> obtenerTodos() throws ExecutionException, InterruptedException {
		return notificacionRepository.obtenerTodos();
	}

	/**
	 * Busca una notificación específica por su ID único.
	 * * @param idNotificacion Identificador de la notificación.
	 * @return La notificación encontrada o null.
	 * @throws NullParamsException Si el ID es nulo.
	 * @throws ExecutionException Si la consulta falla en Firestore.
	 * @throws InterruptedException Si se interrumpe la tarea.
	 */
	@Override
	public Notificacion obtenerPorId(String idNotificacion) throws NullParamsException, ExecutionException, InterruptedException {
		if (idNotificacion == null) {
			throw new NullParamsException("Se requiere un ID para buscar la notificación.");
		}
		return notificacionRepository.obtenerPorId(idNotificacion);
	}

	/**
	 * Elimina una notificación del sistema.
	 * * @param idNotificacion ID de la notificación a borrar.
	 * @throws NullParamsException Si el ID es nulo.
	 * @throws ExecutionException Si el borrado falla en la base de datos.
	 * @throws InterruptedException Si el hilo es interrumpido.
	 * @throws IOException Si falla el registro en el log local.
	 */
	@Override
	public void eliminar(String idNotificacion) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (idNotificacion == null) {
			throw new NullParamsException("Es necesario un ID para eliminar la notificación.");
		}
		notificacionRepository.eliminar(idNotificacion);
	}

	/**
	 * Modifica los datos de una notificación (por ejemplo, para marcarla como leída).
	 * * @param notificacion Objeto con los datos actualizados.
	 * @throws NullParamsException Si el objeto o su ID son nulos.
	 * @throws ExecutionException Si la actualización falla en el servidor.
	 * @throws InterruptedException Si se interrumpe la conexión.
	 * @throws IOException Si ocurre un error de escritura en el log.
	 */
	@Override
	public void modificar(Notificacion notificacion) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (notificacion == null || notificacion.getIdNotificacion() == null) {
			throw new NullParamsException("Datos insuficientes para modificar la notificación.");
		}
		notificacionRepository.modificar(notificacion);
	}
}