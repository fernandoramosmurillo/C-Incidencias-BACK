package com.cIncidencias.api.modelos;

import com.cIncidencias.api.modelos.Serializadores.TimestampDeserializer;
import com.cIncidencias.api.modelos.Serializadores.TimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.cloud.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data // Getters y Setters automáticos para mantener el código limpio
@EqualsAndHashCode(callSuper = true)
public class Valoracion extends ModeloBase{

    private String idValoracion;      // El identificador único de la reseña
    private String nombre;            // El título o resumen (ej: "Muy rápido")
    private String opinion;           // El texto detallado con la experiencia del ciudadano
    private Integer puntuacion; // La nota numérica (rango: de 1 a 5)
    
    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp fechaPublicacion;

    /**
     * Constructor para crear una valoración detallada.
     * Registra el feedback del ciudadano tras cerrar una incidencia, incluyendo 
     * la nota numérica y el comentario que servirá para medir la calidad del servicio.
     */
	public Valoracion(Estados estado, String idValoracion, String nombre, String opinion, Integer puntuacionP,
			Timestamp fechaPublicacion) {
		super(estado);
		this.idValoracion = idValoracion;
		this.nombre = nombre;
		this.opinion = opinion;
		this.puntuacion = puntuacionP;
		this.fechaPublicacion = fechaPublicacion;
	}
}