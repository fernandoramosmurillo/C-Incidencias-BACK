package com.cIncidencias.api.servicios;

import java.util.List;
import com.cIncidencias.api.modelos.ModeloBase.Estados;

/**
 * Interfaz genérica para la capa de servicios del sistema.
 * Define el contrato que deben seguir todos los servicios para garantizar 
 * que la lógica de negocio y las validaciones se apliquen de forma consistente 
 * antes de interactuar con la base de datos.
 * * @param <T> El modelo de datos (Comentario, Incidencia, Usuario, etc.)
 */
public interface IGenericoService<T> {
    
    /**
     * Procesa y valida un objeto antes de solicitar su persistencia en el sistema.
     */
    void guardar(T objeto) throws Exception;
    
    /**
     * Recupera el listado completo de elementos procesados por la lógica de negocio.
     */
    List<T> obtenerTodos() throws Exception;
    
    /**
     * Busca un elemento específico por su identificador, aplicando las validaciones pertinentes.
     */
    T obtenerPorId(String id) throws Exception;
    
    /**
     * Gestiona la solicitud de eliminación física de un registro.
     */
    void eliminar(String id) throws Exception;
    
    /**
     * Valida los cambios en un objeto existente antes de autorizar su modificación.
     */
    void modificar(T objeto) throws Exception;

    /**
     * Coordina el cambio de estado de una entidad (activación, bloqueo o borrado lógico).
     * @param id El identificador único de la entidad.
     * @param estado El nuevo estado que se desea aplicar.
     */
	void cambiarEstado(String id, Estados estado) throws Exception;
			
}