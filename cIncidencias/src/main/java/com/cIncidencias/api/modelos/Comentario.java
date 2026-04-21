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
@Data // Getters y Setters automáticos para mantener el código limpio y ordenado
@EqualsAndHashCode(callSuper = true)
public class Comentario extends ModeloBase {

    private String idComentario;      // El identificador único de cada mensaje
    private String texto;               // El contenido del mensaje (lo que escribe el usuario)
    
    // Al igual que en el resto del proyecto, usamos el tiempo oficial de Google Cloud
    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp fechaPublicacion; 
    
    // Indica si el comentario es solo para operarios/admin o si es público para el ciudadano
    private Boolean esPrivado;        
    
    // El objeto Usuario que escribió el mensaje (Ciudadano, Operario o Admin)
    @JsonSerialize(using = DocumentReferenceSerializer.class)
    @JsonDeserialize(using = DocumentReferenceDeserializer.class)
    private DocumentReference usuarioAutor;

    /**
     * Constructor para inicializar un comentario. 
     * Recoge tanto el contenido del mensaje como los metadatos de autoría y privacidad 
     * necesarios para que se muestren correctamente en el hilo de la incidencia.
     */
	public Comentario(String idComentario, String texto, Timestamp fechaPublicacion, Boolean esPrivado,
			DocumentReference usuarioAutor) {
		super();
		this.idComentario = idComentario;
		this.texto = texto;
		this.fechaPublicacion = fechaPublicacion;
		this.esPrivado = esPrivado;
		this.usuarioAutor = usuarioAutor;
	}
}