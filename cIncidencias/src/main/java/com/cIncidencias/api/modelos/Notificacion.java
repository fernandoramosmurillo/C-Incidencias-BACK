package com.cIncidencias.api.modelos;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.cloud.firestore.DocumentReference;

import lombok.Data;

// Origen de la alerta para saber si viene de la propia incidencia, del sistema o de otro usuario
enum TiposOrigen { SISTEMA, INCIDENCIA, USUARIO }

@Data // Getters y Setters automáticos para mantener el código más limpio y ordenado
public class Notificacion extends ModeloBase{

    private String idNotificacion;   // El identificador único de la notificación
    private String titulo;           // El titular del aviso (ej: "Nueva actualización")
    private String mensaje;          // Cuerpo detallado con la información de la alerta
    
    // Lista de IDs de los usuarios que deben recibir este aviso
    private List<String> idDestinatario; 
    
    private TiposOrigen tipoOrigen;  // Clasificación según quién o qué genera la alerta
    @JsonDeserialize(using = DocumentReferenceDeserializer.class)
    private DocumentReference idOrigen;         // ID específico del disparador (ej: el idIncidencia)
    
	public Notificacion(Estados estado, String idNotificacion, String titulo, String mensaje,
			List<String> idDestinatario, TiposOrigen tipoOrigen, DocumentReference idOrigen) {
		super(estado);
		this.idNotificacion = idNotificacion;
		this.titulo = titulo;
		this.mensaje = mensaje;
		this.idDestinatario = idDestinatario;
		this.tipoOrigen = tipoOrigen;
		this.idOrigen = idOrigen;
	}

	public Notificacion(Estados estado) {
		super(estado);
	}
    
    
}