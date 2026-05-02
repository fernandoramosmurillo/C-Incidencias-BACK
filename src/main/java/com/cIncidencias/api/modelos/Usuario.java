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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Enumeraciones para la gestión de permisos y métodos de autenticación.
 */
enum RolesUsuario { ADMINISTRADOR, OPERARIO, CIUDADANO }
enum TiposAcceso { CORREO_CONTRASEÑA, GOOGLE, ANDROID, IOS, CLAVE_ADMIN }

@AllArgsConstructor
@Data // Genera los Getters y Setters para mantener el código más limpio y ordenado
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Usuario extends ModeloBase {
    
    // Identificadores y datos de contacto del perfil
    protected String idUsuario;          // Identificador único del registro
    protected String nombre;               // Nombre del usuario
    protected String apellidos;          // Apellidos del usuario
    protected String correoElectronico;  // Dirección de correo principal
    protected String clave;               // Credencial de acceso al sistema
    
    // Al igual que en otras clases, se utilizan Timestamps de Google para las fechas
    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    protected Timestamp fechaNacimiento;  
    
    protected RolesUsuario rolUsuario;   // Nivel de privilegios asignado
    protected String fotoPerfilUrl;      // Ruta de la imagen de perfil en el servidor
    protected TiposAcceso tipoAcceso;    // Origen de la autenticación
    
    // Estados lógicos y preferencias del perfil
    protected Boolean bloqueado;          // Indica si el acceso está restringido
    protected Boolean recibirNotificaciones; // Consentimiento para el envío de alertas
    
    // Ciclo de vida de la cuenta
    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    protected Timestamp fechaCreacion;
    
    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    protected Timestamp fechaEliminacion; 
    
    // Registro histórico de las comunicaciones enviadas al usuario
    @JsonSerialize(contentUsing = DocumentReferenceSerializer.class)
    @JsonDeserialize(contentUsing = DocumentReferenceDeserializer.class)
    protected List<DocumentReference> notificacionesRecibidas = new ArrayList<>();

    /**
     * Constructor base para la entidad Usuario. 
     * Recoge toda la información común de los perfiles del sistema. He incluido la 
     * validación de la lista de notificaciones para asegurar que, aunque la cuenta 
     * sea nueva, el objeto esté listo para registrar avisos sin fallos de puntero nulo.
     */
	public Usuario(Estados estado, String idUsuario, String nombre, String apellidos, String correoElectronico,
			String clave, Timestamp fechaNacimiento, RolesUsuario rolUsuario, String fotoPerfilUrl,
			TiposAcceso tipoAcceso, Boolean bloqueado, Boolean recibirNotificaciones, Timestamp fechaCreacion,
			Timestamp fechaEliminacion, List<DocumentReference> notificacionesRecibidas) {
		super(estado);
		this.idUsuario = idUsuario;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.correoElectronico = correoElectronico;
		this.clave = clave;
		this.fechaNacimiento = fechaNacimiento;
		this.rolUsuario = rolUsuario;
		this.fotoPerfilUrl = fotoPerfilUrl;
		this.tipoAcceso = tipoAcceso;
		this.bloqueado = bloqueado;
		this.recibirNotificaciones = recibirNotificaciones;
		this.fechaCreacion = fechaCreacion;
		this.fechaEliminacion = fechaEliminacion;
        
        // Si el listado viene nulo de la base de datos, lo inicializamos vacío
		this.notificacionesRecibidas = (notificacionesRecibidas != null) ? notificacionesRecibidas : new ArrayList<>();
	}
}