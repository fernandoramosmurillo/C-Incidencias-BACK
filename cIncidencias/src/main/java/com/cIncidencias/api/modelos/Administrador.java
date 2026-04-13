package com.cIncidencias.api.modelos;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.cloud.Timestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// Diferentes áreas que puede gestionar un administrador en el ayuntamiento
enum Departamentos {
	D_ELECTRICIDAD, D_FONTANERIA, D_OBRAS_PUBLICAS, D_JARDINERIA, D_LIMPIEZA_VIARIA, D_CARPINTERIA, D_PINTURA,
	D_CERRAJERIA, D_ALBAÑILERIA
}

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true) // Comprueba los datos del padre (Usuario) al hacer comparaciones
public class Administrador extends Usuario {

	// Controles de seguridad y niveles de gestión
	private Integer nivelAcceso; // Rango de poder dentro del panel de control
	private Integer intentosPushDenegados; // Registro de seguridad sobre login fallidos
	private Boolean esSuperAdmin; // Indica si el administrador es un administrador global, es decir, puede
									// eliminar otros administradores y esta excepto de credenciales
	private String firmaDigitalId; // Identificador para validar documentos u órdenes oficiales

	private Departamentos departamento; // El área específica asignada al administrador

	@JsonDeserialize(using = TimestampDeserializer.class)
	private Timestamp fechaCambioClave;

	private String claveAdmin; // Contraseña especial para las administradores

	// Historial de noticias o bandos publicados por este administrador
	private List<Noticia> listaNoticiasEnviadas;

	public Administrador(Estados estado, String idUsuario, String nombre, String apellidos, String correoElectronico,
			String clave, Timestamp fechaNacimiento, RolesUsuario rolUsuario, String fotoPerfilUrl,
			TiposAcceso tipoAcceso, Boolean bloqueado, Boolean recibirNotificaciones, Timestamp fechaCreacion,
			Timestamp fechaEliminacion, List<Notificacion> notificacionesRecibidas, Integer nivelAcceso,
			Integer intentosPushDenegados, Boolean esSuperAdmin, String firmaDigitalId, Departamentos departamento,
			Timestamp fechaCambioClave, String claveAdmin, List<Noticia> listaNoticiasEnviadas) {
		super(estado, idUsuario, nombre, apellidos, correoElectronico, clave, fechaNacimiento, rolUsuario,
				fotoPerfilUrl, tipoAcceso, bloqueado, recibirNotificaciones, fechaCreacion, fechaEliminacion,
				notificacionesRecibidas);
		this.nivelAcceso = nivelAcceso;
		this.intentosPushDenegados = intentosPushDenegados;
		this.esSuperAdmin = esSuperAdmin;
		this.firmaDigitalId = firmaDigitalId;
		this.departamento = departamento;
		this.fechaCambioClave = fechaCambioClave;
		this.claveAdmin = claveAdmin;
		this.listaNoticiasEnviadas = listaNoticiasEnviadas;
	}
}