package com.cIncidencias.api.modelos;

/**
 * Base para la gestión de estados y borrado lógico de las entidades.
 */
public abstract class ModeloBase {

    public enum Estados { ACTIVO, ELIMINADO, EN_BORRADOR, BLOQUEADO, INACTIVO }

    protected Estados estado = Estados.ACTIVO;

    /**
     * Marca la entidad como eliminada de forma temporal.
     * No se borra de la base de datos, solo cambia su estado a ELIMINADO.
     */
    public void eliminarTemporalmente() { this.estado = Estados.ELIMINADO; }

    /** Restaura el estado de la entidad a ACTIVO. */
    public void recuperar() { this.estado = Estados.ACTIVO; }

    /** Cambia el estado a EN_BORRADOR para terminar formularios incompletos. */
    public void ponerEnBorrador() { this.estado = Estados.EN_BORRADOR; }

    /** @return El estado actual de la entidad. */
    public Estados getEstado() { return estado; }

    /** @param estado El nuevo estado a asignar. */
    public void setEstado(Estados estado) { this.estado = estado; }

    public ModeloBase(Estados estado) {
        super();
        this.estado = estado;
    }

    public ModeloBase() {
        super();
    }
}