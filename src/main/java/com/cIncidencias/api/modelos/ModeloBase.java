package com.cIncidencias.api.modelos;

import lombok.AllArgsConstructor;

/**
 * Base para la gestión de estados y borrado lógico de las entidades.
 */

@AllArgsConstructor
public abstract class ModeloBase {

    public enum Estados { ACTIVO, ELIMINADO, EN_BORRADOR, BLOQUEADO, INACTIVO }

    protected Estados estado = Estados.ACTIVO;

    /**
     * Cambia el estado de la entidad a ELIMINADO. 
     * Es un borrado lógico: la info sigue en la base de datos por seguridad, 
     * pero deja de estar visible en la aplicación.
     */
    public void eliminarTemporalmente() { this.estado = Estados.ELIMINADO; }

    /** * Vuelve a poner la entidad en estado ACTIVO. 
     * Útil para cuando un administrador quiere deshacer un borrado accidental.
     */
    public void recuperar() { this.estado = Estados.ACTIVO; }

    /** * Marca la entidad como un borrador. 
     * Sirve para guardar datos provisionales sin que lleguen a publicarse 
     * oficialmente en el sistema.
     */
    public void ponerEnBorrador() { this.estado = Estados.EN_BORRADOR; }

    /** @return El estado actual en el que se encuentra la entidad. */
    public Estados getEstado() { return estado; }

    /** @param estado El nuevo estado que queremos asignar manualmente. */
    public void setEstado(Estados estado) { this.estado = estado; }

    /**
     * Constructor por defecto. Por norma general, cualquier entidad 
     * nueva arrancará en estado ACTIVO.
     */
    public ModeloBase() {
        super();
    }
}