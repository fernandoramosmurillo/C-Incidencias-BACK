package com.cIncidencias.api.modelos;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data // Getters y Setters automáticos para un código más limpio y profesional
public class Noticia {

    private String idNoticia;       // El identificador único del artículo
    private String titulo;          // Titular principal de la noticia
    private String entradilla;      // Un pequeño resumen para enganchar al lector
    private String contenido;       // El cuerpo completo de la noticia (admite Markdown)
    
    private String urlImagenPortada; // El link a la foto principal en la nube
    private String categoria;       // Ej: "Avisos", "Eventos", "Cortes de tráfico"
    
    // Al igual que en todo el proyecto, usamos el Timestamp de Google Cloud
    private Timestamp fechaPublicacion; 
    
    private String idAutor;         // El ID del Administrador que redactó la noticia
    private Boolean esDestacada;    // Si aparece la primera o con un diseño especial en la app
}