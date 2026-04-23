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

	/**
	 * Valida y registra una nueva notificación.
	 * Verifica que el mensaje no esté vacío y que exista al menos un ID de destinatario 
	 * para garantizar que la alerta no se pierda en el sistema.
	 * @param notificacion Objeto Notificacion a guardar.
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
		
		// Lógica de negocio: Se verifica que tiene al menos un usuario de destino
		if (notificacion.getIdDestinatarios() == null || notificacion.getIdDestinatarios().isEmpty()) {
			throw new NullParamsException("La notificación debe tener al menos un usuario de destino.");
		}

		notificacionRepository.guardar(notificacion);
	}

	/**
	 * Recupera el historial de todas las notificaciones emitidas por el sistema.
	 * @return Lista de objetos Notificacion.
	 * @throws ExecutionException Si hay un fallo al obtener datos del servidor.
	 * @throws InterruptedException Si se corta la conexión con Firestore.
	 */
	@Override
	public List<Notificacion> obtenerTodos() throws ExecutionException, InterruptedException {
		return notificacionRepository.obtenerTodos();
	}

	/**
	 * Busca una notificación específica por su ID único.
	 * @param idNotificacion Identificador de la notificación.
	 * @return La notificación encontrada o null si no existe.
	 * @throws NullParamsException Si el ID es nulo o está vacío.
	 * @throws ExecutionException Si la consulta falla en Firestore.
	 * @throws InterruptedException Si se interrumpe la tarea.
	 */
	@Override
	public Notificacion obtenerPorId(String idNotificacion) throws NullParamsException, ExecutionException, InterruptedException {
		if (idNotificacion == null || idNotificacion.trim().isEmpty()) {
			throw new NullParamsException("Se requiere un ID válido para buscar la notificación.");
		}
		return notificacionRepository.obtenerPorId(idNotificacion);
	}

	/**
	 * Elimina permanentemente una notificación del sistema tras validar su existencia.
	 * @param idNotificacion ID de la notificación a borrar.
	 * @throws NullParamsException Si el ID es nulo o está vacío.
	 * @throws ExecutionException Si el borrado falla en la base de datos.
	 * @throws InterruptedException Si el hilo es interrumpido.
	 * @throws IOException Si falla el registro en el log local.
	 */
	@Override
	public void eliminar(String idNotificacion) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (idNotificacion == null || idNotificacion.trim().isEmpty()) {
			throw new NullParamsException("Es necesario un ID válido para eliminar la notificación.");
		}
		notificacionRepository.eliminar(idNotificacion);
	}

	/**
	 * Modifica los datos de una notificación (por ejemplo, para actualizar el cuerpo del aviso).
	 * @param notificacion Objeto con los datos actualizados e ID válido.
	 * @throws NullParamsException Si el objeto o su ID son nulos o vacíos.
	 * @throws ExecutionException Si la actualización falla en el servidor.
	 * @throws InterruptedException Si se interrumpe la conexión.
	 * @throws IOException Si ocurre un error de escritura en el log.
	 */
	@Override
	public void modificar(Notificacion notificacion) throws NullParamsException, ExecutionException, InterruptedException, IOException {
		if (notificacion == null || notificacion.getIdNotificacion() == null || notificacion.getIdNotificacion().trim().isEmpty()) {
			throw new NullParamsException("Datos insuficientes para modificar la notificación (ID ausente).");
		}
		notificacionRepository.modificar(notificacion);
	}
	
	/**
	 * Gestiona el cambio de estado de la notificación (por ejemplo, para marcarla como LEÍDA o ARCHIVADA).
	 * Valida que el identificador sea correcto antes de proceder con el cambio en el repositorio.
	 */
	@Override
	public void cambiarEstado(String idNotificacion, ModeloBase.Estados estado) throws Exception {

	    if (idNotificacion == null || idNotificacion.trim().isEmpty()) {
	        throw new NullParamsException("Se necesita un ID válido para cambiar el estado de la notificación.");
	    }

	    notificacionRepository.cambiarEstado(idNotificacion, estado);
	}
}