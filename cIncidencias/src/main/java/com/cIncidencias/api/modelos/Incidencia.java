package com.cIncidencias.api.modelos;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.GeoPoint;

import lombok.Data;

@Data // Genera los Getters y Setters para mantener el codigo más limpio y ordenado
public class Incidencia {
    
	enum Prioridades {MINIMO,BAJA,MEDIA,ALTA,MUY_ALTA,URGENTE}
	
    private String idIncidencia; // El DNI de la incidencia
    private String nombre;      // El título (ej: "Bache en el centro")
    private String descripcion; // Descripcion detallada de lo que esta pasando en la incidencia
    private GeoPoint ubicacion;   // Ubicacion de la incidencia, Geopoint pertenece a google cloud y devuelve las cordenadas exactas
    private String imagenUrl;   // El link a la foto que suban a la nube
    
    // Las fechas en Firebase no son "Date" normales, son Timestamps de Google
    private Timestamp fechaCreacion; 
    private Timestamp fechaCierre;
    
    // El ciudadano que puso la queja
    private Ciudadano usuarioCiudadano; 
    
    // Lista de operarios que se encargan de la incidencia
    private TreeMap<String, OperarioMunicipal> listaOperarios; 
    
    // Un array con todos los 
    private List<Comentario> comentarios;
    
    // Prioridades y Valoracion (Normalmente serán Enums: BAJA, MEDIA, ALTA...)
    private Prioridades prioridad;
    private Valoracion valoracion;
}