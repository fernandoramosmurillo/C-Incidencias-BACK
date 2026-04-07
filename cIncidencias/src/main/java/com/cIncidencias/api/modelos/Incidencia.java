package com.cIncidencias.api.modelos;

import java.util.List;
import java.util.TreeMap;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.GeoPoint;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Representa una incidencia en el sistema. 
 * Hereda el estado de persistencia de ModeloBase y gestiona su propio flujo de trabajo.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Incidencia extends ModeloBase {
    
    /** Estados que definen el ciclo de vida de resolución de una incidencia. */
    public enum EstadosIncidencia { ABIERTA, ASIGNADA, EN_PROCESO, SOLUCIONADA, RECHAZADA }
    
    /** Niveles de urgencia para la atención de la incidencia. */
    public enum Prioridades { MINIMO, BAJA, MEDIA, ALTA, MUY_ALTA, URGENTE }

    private String idIncidencia;
    private String nombre;
    private String descripcion;
    private GeoPoint ubicacion;
    private String imagenUrl;
    
    private Timestamp fechaCreacion; 
    private Timestamp fechaCierre;
    
    private Ciudadano usuarioCiudadano; 
    private TreeMap<String, OperarioMunicipal> listaOperarios; 
    private List<Comentario> comentarios;
    
    // Atributos de categorización propios de la incidencia
    private Prioridades prioridad;
    private EstadosIncidencia estadoIncidencia = EstadosIncidencia.ABIERTA;
    private Valoracion valoracion;
    
	public Incidencia(String idIncidencia, String nombre, String descripcion, GeoPoint ubicacion, String imagenUrl,
			Timestamp fechaCreacion, Timestamp fechaCierre, Ciudadano usuarioCiudadano,
			TreeMap<String, OperarioMunicipal> listaOperarios, List<Comentario> comentarios, Prioridades prioridad,
			EstadosIncidencia estadoIncidencia, Valoracion valoracion) {
		super();
		this.idIncidencia = idIncidencia;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.ubicacion = ubicacion;
		this.imagenUrl = imagenUrl;
		this.fechaCreacion = fechaCreacion;
		this.fechaCierre = fechaCierre;
		this.usuarioCiudadano = usuarioCiudadano;
		this.listaOperarios = listaOperarios;
		this.comentarios = comentarios;
		this.prioridad = prioridad;
		this.estadoIncidencia = estadoIncidencia;
		this.valoracion = valoracion;
	}

	public Incidencia() {
		super();
	}
	
	
    
    

    
}