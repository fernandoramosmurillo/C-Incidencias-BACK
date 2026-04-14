package com.cIncidencias.api.servicios;

import java.util.List;

import com.cIncidencias.api.modelos.ModeloBase.Estados;

/**
 * Interfaz genérica para los servicios.
 * @param <T> El modelo de datos (Comentario, Incidencia, Usuario, etc.)
 */
public interface IGenericoService<T> {
    
    void guardar(T objeto) throws Exception;
    
    List<T> obtenerTodos() throws Exception;
    
    T obtenerPorId(String id) throws Exception;
    
    void eliminar(String id) throws Exception;
    
    void modificar(T objeto) throws Exception;

	void cambiarEstado(String idComentario, Estados estado) throws Exception;
			
}