package com.cIncidencias.api.modelos;

import com.cIncidencias.api.modelos.Serializadores.DocumentReferenceDeserializer;
import com.cIncidencias.api.modelos.Serializadores.DocumentReferenceSerializer;
import com.cIncidencias.api.modelos.Serializadores.TimestampDeserializer;
import com.cIncidencias.api.modelos.Serializadores.TimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data // Getters y Setters automáticos para un código más limpio y profesional
@EqualsAndHashCode(callSuper = true)
public class Noticia extends ModeloBase {

    private String idNoticia;       // El identificador único del artículo
    private String titulo;          // Titular principal de la noticia
    private String entradilla;      // Un pequeño resumen para enganchar al lector
    private String cuerpo;       // El cuerpo completo de la noticia (admite Markdown)
    
    private String urlImagenPortada; // El link a la foto principal en la nube
    private String categoria;       // Ej: "Avisos", "Eventos", "Cortes de tráfico"
    
    // Al igual que en todo el proyecto, usamos el Timestamp de Google Cloud
    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp fechaPublicacion; 
    
    @JsonDeserialize(using = DocumentReferenceDeserializer.class)
    @JsonSerialize(using = DocumentReferenceSerializer.class)
    private DocumentReference idAutor;           // El ID del Administrador que redactó la noticia
    private Boolean esDestacada;    // Si aparece la primera o con un diseño especial en la app
    
    /**
     * Constructor completo para crear una noticia desde cero. 
     * Pasa el estado inicial a la clase padre y mapea todos los campos necesarios 
     * para que el bando o comunicado se visualice correctamente en el tablón de anuncios.
     */
	public Noticia(Estados estado, String idNoticia, String titulo, String entradilla, String contenido,
			String urlImagenPortada, String categoria, Timestamp fechaPublicacion, DocumentReference idAutor,
			Boolean esDestacada) {
		super(estado);
		this.idNoticia = idNoticia;
		this.titulo = titulo;
		this.entradilla = entradilla;
		this.cuerpo = contenido;
		this.urlImagenPortada = urlImagenPortada;
		this.categoria = categoria;
		this.fechaPublicacion = fechaPublicacion;
		this.idAutor = idAutor;
		this.esDestacada = esDestacada;
	}
}