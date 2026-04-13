package com.cIncidencias.api.modelos;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Listado de oficios disponibles para asignar según el tipo de avería.
 */
enum Especialidades { 
    ELECTRICIDAD, FONTANERIA, OBRAS_PUBLICAS, JARDINERIA, 
    LIMPIEZA_VIARIA, CARPINTERIA, PINTURA, CERRAJERIA, ALBAÑILERIA 
}

@Data
@EqualsAndHashCode(callSuper = true) // Se encarga de comprobar los datos del padre cuando haga un equals o hash
public class OperarioMunicipal extends Usuario {
    
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
    private List<DocumentReference> listaIncidenciasAsignadas;

	public OperarioMunicipal(Estados estado, String idUsuario, String nombre, String apellidos,
			String correoElectronico, String clave, Timestamp fechaNacimiento, RolesUsuario rolUsuario,
			String fotoPerfilUrl, TiposAcceso tipoAcceso, Boolean bloqueado, Boolean recibirNotificaciones,
			Timestamp fechaCreacion, Timestamp fechaEliminacion, List<Notificacion> notificacionesRecibidas,
			Especialidades especialidad, Boolean disponible, Boolean carnetConducir, String telefonoTrabajo,
			Integer numeroCuadrilla, Integer incidenciasResueltas, List<DocumentReference> listaIncidenciasAsignadas) {
		super(estado, idUsuario, nombre, apellidos, correoElectronico, clave, fechaNacimiento, rolUsuario,
				fotoPerfilUrl, tipoAcceso, bloqueado, recibirNotificaciones, fechaCreacion, fechaEliminacion,
				notificacionesRecibidas);
		this.especialidad = especialidad;
		this.disponible = disponible;
		this.carnetConducir = carnetConducir;
		this.telefonoTrabajo = telefonoTrabajo;
		this.numeroCuadrilla = numeroCuadrilla;
		this.incidenciasResueltas = incidenciasResueltas;
		this.listaIncidenciasAsignadas = listaIncidenciasAsignadas;
	}

	public OperarioMunicipal(Estados estado, String idUsuario, String nombre, String apellidos,
			String correoElectronico, String clave, Timestamp fechaNacimiento, RolesUsuario rolUsuario,
			String fotoPerfilUrl, TiposAcceso tipoAcceso, Boolean bloqueado, Boolean recibirNotificaciones,
			Timestamp fechaCreacion, Timestamp fechaEliminacion, List<Notificacion> notificacionesRecibidas) {
		super(estado, idUsuario, nombre, apellidos, correoElectronico, clave, fechaNacimiento, rolUsuario,
				fotoPerfilUrl, tipoAcceso, bloqueado, recibirNotificaciones, fechaCreacion, fechaEliminacion,
				notificacionesRecibidas);
	}

	
    
}