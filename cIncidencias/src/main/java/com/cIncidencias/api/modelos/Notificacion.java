package com.cIncidencias.api.modelos;

import java.util.ArrayList;
import java.util.List;

import com.cIncidencias.api.modelos.Serializadores.DocumentReferenceDeserializer;
import com.cIncidencias.api.modelos.Serializadores.DocumentReferenceSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.cloud.firestore.DocumentReference;

import lombok.Data;
import lombok.NoArgsConstructor;

// Origen de la alerta para saber si viene de la propia incidencia, del sistema o de otro usuario
enum TiposOrigen { SISTEMA, INCIDENCIA, USUARIO }

@NoArgsConstructor
@Data // Getters y Setters automáticos para mantener el código más limpio y ordenado
public class Notificacion extends ModeloBase {

    private String idNotificacion;   // El identificador único de la notificación
    private String titulo;            // El titular del aviso (ej: "Nueva actualización")
    private String mensaje;           // Cuerpo detallado con la información de la alerta
    
    // Lista de IDs de los usuarios que deben recibir este aviso
    @JsonSerialize(contentUsing = DocumentReferenceSerializer.class)
    @JsonDeserialize(contentUsing = DocumentReferenceDeserializer.class)
    private List<DocumentReference> idDestinatarios = new ArrayList<>();
    
    private TiposOrigen tipoOrigen;  // Clasificación según quién o qué genera la alerta
    @JsonDeserialize(using = DocumentReferenceDeserializer.class)
    @JsonSerialize(using = DocumentReferenceSerializer.class)
    private DocumentReference idOrigen;         // ID específico del disparador (ej: el idIncidencia)
    
    public Notificacion(Estados estado, String idNotificacion, String titulo, String mensaje,
            List<DocumentReference> idDestinatario, TiposOrigen tipoOrigen, DocumentReference idOrigen) {
        super(estado);
        this.idNotificacion = idNotificacion;
        this.titulo = titulo;
        this.mensaje = mensaje;
        // Si el valor recibido es nulo, se asigna una lista vacía para evitar errores
        this.idDestinatarios = (idDestinatario != null) ? idDestinatario : new ArrayList<>();
        this.tipoOrigen = tipoOrigen;
        this.idOrigen = idOrigen;
    }
}