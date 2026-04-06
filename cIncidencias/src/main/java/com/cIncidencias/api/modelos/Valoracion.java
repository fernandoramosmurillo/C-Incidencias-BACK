package com.cIncidencias.api.modelos;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data // Getters y Setters automáticos para mantener el código limpio
public class Valoracion {

    private String idValoracion;      // El identificador único de la reseña
    private String nombre;            // El título o resumen (ej: "Muy rápido")
    private String opinion;           // El texto detallado con la experiencia del ciudadano
    private Integer puntuacionEstrellas; // La nota numérica (rango: de 1 a 5)
    
    private Timestamp fechaPublicacion; 
}