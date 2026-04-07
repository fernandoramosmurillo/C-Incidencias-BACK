package com.cIncidencias.api.modelos;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data // Getters y Setters automáticos para mantener el código limpio
public class Valoracion extends ModeloBase{

    private String idValoracion;      // El identificador único de la reseña
    private String nombre;            // El título o resumen (ej: "Muy rápido")
    private String opinion;           // El texto detallado con la experiencia del ciudadano
    private Integer puntuacionEstrellas; // La nota numérica (rango: de 1 a 5)
    
    private Timestamp fechaPublicacion;

	public Valoracion(Estados estado, String idValoracion, String nombre, String opinion, Integer puntuacionEstrellas,
			Timestamp fechaPublicacion) {
		super(estado);
		this.idValoracion = idValoracion;
		this.nombre = nombre;
		this.opinion = opinion;
		this.puntuacionEstrellas = puntuacionEstrellas;
		this.fechaPublicacion = fechaPublicacion;
	}

	public Valoracion(Estados estado) {
		super(estado);
	} 
    
    
}