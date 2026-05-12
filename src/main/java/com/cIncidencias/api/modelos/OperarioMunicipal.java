package com.cIncidencias.api.modelos;

import java.util.ArrayList;
import java.util.List;

import com.cIncidencias.api.modelos.Serializadores.DocumentReferenceDeserializer;
import com.cIncidencias.api.modelos.Serializadores.DocumentReferenceSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Listado de oficios disponibles para asignar según el tipo de avería.
 */
enum Especialidades { 
    ELECTRICIDAD, FONTANERIA, OBRAS_PUBLICAS, JARDINERIA, 
    LIMPIEZA_VIARIA, CARPINTERIA, PINTURA, CERRAJERIA, ALBAÑILERIA 
}

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true) // Se encarga de comprobar los datos del padre cuando haga un equals o hash
public class OperarioMunicipal extends Ciudadano {
    
    // Atributos específicos del puesto de trabajo
    private Especialidades especialidad; // El oficio principal del operario
    private Boolean disponible;          // Indica si está libre para recibir avisos
    private Boolean carnetConducir;      // Para saber si puede llevar el vehículo oficial
    private String telefonoTrabajo;      // El número corporativo para emergencias
    
    // Datos de organización y rendimiento
    private Integer numeroCuadrilla;     // El grupo de trabajo al que pertenece
    private Integer incidenciasResueltas; // Contador de trabajos finalizados con éxito
    
    // Relación directa con las incidencias que tiene entre manos
    @JsonDeserialize(contentUsing = DocumentReferenceDeserializer.class)
    @JsonSerialize(contentUsing = DocumentReferenceSerializer.class)
    private List<DocumentReference> listaIncidenciasAsignadas = new ArrayList<>();

    /**
     * Constructor para dar de alta a un operario con su perfil laboral completo.
     * He mantenido la validación al final para que la lista de incidencias asignadas 
     * nunca llegue como null; así, si el operario es nuevo y no tiene tareas, 
     * la lista se queda lista para añadir elementos sin lanzar excepciones.
     */
	public OperarioMunicipal(Estados estado, String idUsuario, String nombre, String apellidos,
			String correoElectronico, String clave, Timestamp fechaNacimiento, RolesUsuario rolUsuario,
			String fotoPerfilUrl, TiposAcceso tipoAcceso, Boolean bloqueado, Boolean recibirNotificaciones,
			Timestamp fechaCreacion, Timestamp fechaEliminacion, List<DocumentReference> notificacionesRecibidas,
			Especialidades especialidad, Boolean disponible, Boolean carnetConducir, String telefonoTrabajo,
			Integer numeroCuadrilla, Integer incidenciasResueltas, List<DocumentReference> listaIncidenciasAsignadas) {
		super(estado, idUsuario, nombre, apellidos, correoElectronico, clave, fechaNacimiento, rolUsuario,
				fotoPerfilUrl, tipoAcceso, bloqueado, recibirNotificaciones, fechaCreacion, fechaEliminacion,
				notificacionesRecibidas, telefonoTrabajo, telefonoTrabajo, telefonoTrabajo, listaIncidenciasAsignadas, listaIncidenciasAsignadas);
		this.especialidad = especialidad;
		this.disponible = disponible;
		this.carnetConducir = carnetConducir;
		this.telefonoTrabajo = telefonoTrabajo;
		this.numeroCuadrilla = numeroCuadrilla;
		this.incidenciasResueltas = incidenciasResueltas;
        
        // Si no hay incidencias asignadas todavía, se inicializa la lista vacía
		this.listaIncidenciasAsignadas = (listaIncidenciasAsignadas != null) ? listaIncidenciasAsignadas : new ArrayList<>();
	}
}