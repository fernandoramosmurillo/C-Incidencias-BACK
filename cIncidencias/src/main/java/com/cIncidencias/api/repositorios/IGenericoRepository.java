package com.cIncidencias.api.repositorios;

import java.util.List;
import com.cIncidencias.api.modelos.ModeloBase.Estados;

/**
 * Interfaz genérica para los repositorios. Define las operaciones CRUD básicas.
 * @param <T> El tipo de modelo (Incidencia, Usuario, Noticia, etc.)
 */
public interface IGenericoRepository<T> {
    
    void guardar(T objeto) throws Exception;
    
    List<T> obtenerTodos() throws Exception;
    
    T obtenerPorId(String id) throws Exception;
    
    void eliminar(String id) throws Exception;
    
    void modificar(T objeto) throws Exception;

	void cambiarEstado(String idComentario, Estados estado) throws Exception;
			
}