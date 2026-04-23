package com.cIncidencias.api.modelos;

import java.util.ArrayList;
import java.util.List;

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

	@JsonSerialize(using = TimestampSerializer.class)
	@JsonDeserialize(using = TimestampDeserializer.class)
	private Timestamp fechaCambioClave;

	private String claveAdmin; // Contraseña especial para las administradores

	// Historial de noticias o bandos publicados por este administrador
	// Inicializamos con ArrayList para evitar NullPointerException en el resto de la app
	@JsonSerialize(contentUsing = DocumentReferenceSerializer.class)
	@JsonDeserialize(contentUsing = DocumentReferenceDeserializer.class)
	private List<DocumentReference> listaNoticiasEnviadas = new ArrayList<>();

	/**
	 * Constructor completo para crear un administrador con todos sus permisos y datos de seguridad.
	 * He incluido la validación al final para que la lista de noticias nunca nos llegue como null 
	 * y no nos rompa la ejecución más adelante.
	 */
	public Administrador(Estados estado, String idUsuario, String nombre, String apellidos, String correoElectronico,
			String clave, Timestamp fechaNacimiento, RolesUsuario rolUsuario, String fotoPerfilUrl,
			TiposAcceso tipoAcceso, Boolean bloqueado, Boolean recibirNotificaciones, Timestamp fechaCreacion,
			Timestamp fechaEliminacion, List<DocumentReference> notificacionesRecibidas, Integer nivelAcceso,
			Integer intentosPushDenegados, Boolean esSuperAdmin, String firmaDigitalId, Departamentos departamento,
			Timestamp fechaCambioClave, String claveAdmin, List<DocumentReference> listaNoticiasEnviadas) {
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
		
		// Validación para asegurar que la lista nunca sea null tras la construcción
		this.listaNoticiasEnviadas = (listaNoticiasEnviadas != null) ? listaNoticiasEnviadas : new ArrayList<>();
	}
}