package com.cIncidencias.api.repositorios;

import java.util.List;
import com.cIncidencias.api.modelos.ModeloBase.Estados;

/**
 * Interfaz genérica para los repositorios. Define las operaciones CRUD básicas.
 * La usamos para asegurar que todos nuestros repositorios (Usuarios, Incidencias, etc.) 
 * sigan las mismas reglas y compartan los mismos métodos fundamentales.
 * * @param <T> El tipo de modelo con el que trabajará el repositorio.
 */
public interface IGenericoRepository<T> {
    
    /**
     * Registra un nuevo objeto en la base de datos de Firestore.
     */
    void guardar(T objeto) throws Exception;
    
    /**
     * Recupera la lista completa de registros de la colección correspondiente.
     */
    List<T> obtenerTodos() throws Exception;
    
    /**
     * Busca un registro específico utilizando su identificador único.
     */
    T obtenerPorId(String id) throws Exception;
    
    /**
     * Realiza una eliminación física del documento en la base de datos.
     */
    void eliminar(String id) throws Exception;
    
    /**
     * Actualiza la información de un registro ya existente.
     */
    void modificar(T objeto) throws Exception;

    /**
     * Permite actualizar únicamente el estado lógico de una entidad 
     * (por ejemplo, pasar de ACTIVO a BLOQUEADO) sin tocar el resto de datos.
     */
	void cambiarEstado(String id, Estados estado) throws Exception;
			
}