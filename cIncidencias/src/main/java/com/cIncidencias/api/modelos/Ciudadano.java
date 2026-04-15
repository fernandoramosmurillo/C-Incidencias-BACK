package com.cIncidencias.api.modelos;

import java.util.ArrayList;
import java.util.List;
import com.google.cloud.Timestamp;
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
    private List<Incidencia> incidenciasSolicitadas = new ArrayList<>(); 
    private List<Incidencia> incidenciasCalificadas = new ArrayList<>();
    
    public Ciudadano(Estados estado, String idUsuario, String nombre, String apellidos, String correoElectronico,
            String clave, Timestamp fechaNacimiento, RolesUsuario rolUsuario, String fotoPerfilUrl,
            TiposAcceso tipoAcceso, Boolean bloqueado, Boolean recibirNotificaciones, Timestamp fechaCreacion,
            Timestamp fechaEliminacion, List<Notificacion> notificacionesRecibidas, String dni,
            Integer telefonoContacto, String direccion, List<Incidencia> incidenciasSolicitadas,
            List<Incidencia> incidenciasCalificadas) {
        
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