package com.cIncidencias.api.modelos;

import java.util.ArrayList;
import java.util.List;

import com.cIncidencias.api.modelos.Serializadores.DocumentReferenceDeserializer;
import com.cIncidencias.api.modelos.Serializadores.DocumentReferenceSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Ciudadano extends Usuario {

    private String dni;
    private Integer telefonoContacto;
    private String direccion;
    
    // Inicialización directa para evitar NullPointerException
    @JsonSerialize(contentUsing = DocumentReferenceSerializer.class)
    @JsonDeserialize(contentUsing = DocumentReferenceDeserializer.class)
    private List<DocumentReference> incidenciasSolicitadas = new ArrayList<>(); 

    @JsonSerialize(contentUsing = DocumentReferenceSerializer.class)
    @JsonDeserialize(contentUsing = DocumentReferenceDeserializer.class)
    private List<DocumentReference> incidenciasCalificadas = new ArrayList<>();
    
    public Ciudadano(Estados estado, String idUsuario, String nombre, String apellidos, String correoElectronico,
            String clave, Timestamp fechaNacimiento, RolesUsuario rolUsuario, String fotoPerfilUrl,
            TiposAcceso tipoAcceso, Boolean bloqueado, Boolean recibirNotificaciones, Timestamp fechaCreacion,
            Timestamp fechaEliminacion, List<DocumentReference> notificacionesRecibidas, String dni,
            Integer telefonoContacto, String direccion, List<DocumentReference> incidenciasSolicitadas,
            List<DocumentReference> incidenciasCalificadas) {
        
        super(estado, idUsuario, nombre, apellidos, correoElectronico, clave, fechaNacimiento, rolUsuario,
                fotoPerfilUrl, tipoAcceso, bloqueado, recibirNotificaciones, fechaCreacion, fechaEliminacion,
                notificacionesRecibidas);
        
        this.dni = dni;
        this.telefonoContacto = telefonoContacto;
        this.direccion = direccion;
        
        // Validación en el constructor para asegurar que si pasan null por parámetro, se mantenga la lista vacía
        this.incidenciasSolicitadas = (incidenciasSolicitadas != null) ? incidenciasSolicitadas : new ArrayList<>();
        this.incidenciasCalificadas = (incidenciasCalificadas != null) ? incidenciasCalificadas : new ArrayList<>();
    } 
}