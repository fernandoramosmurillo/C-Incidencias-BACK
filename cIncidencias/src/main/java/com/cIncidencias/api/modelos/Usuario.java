package com.cIncidencias.api.modelos;

import java.util.List;
import com.google.cloud.Timestamp;
import lombok.Data;

/**
 * Enumeraciones para la gestión de permisos y métodos de autenticación.
 */
enum RolesUsuario { ADMINISTRADOR, OPERARIO, CIUDADANO }
enum TiposAcceso { CORREO_CONTRASEÑA, GOOGLE, CLAVE_ADMIN }

@Data // Genera los Getters y Setters para mantener el código más limpio y ordenado
public class Usuario {
    
    // Identificadores y datos de contacto del perfil
    protected String idUsuario;          // Identificador único del registro
    protected String nombre;             // Nombre del usuario
    protected String apellidos;          // Apellidos del usuario
    protected String correoElectronico;  // Dirección de correo principal
    protected String clave;              // Credencial de acceso al sistema
    
    // Al igual que en otras clases, se utilizan Timestamps de Google para las fechas
    protected Timestamp fechaNacimiento;  
    
    protected RolesUsuario rolUsuario;   // Nivel de privilegios asignado
    protected String fotoPerfilUrl;      // Ruta de la imagen de perfil en el servidor
    protected TiposAcceso tipoAcceso;    // Origen de la autenticación
    
    // Estados lógicos y preferencias del perfil
    protected Boolean bloqueado;          // Indica si el acceso está restringido
    protected Boolean recibirNotificaciones; // Consentimiento para el envío de alertas
    
    // Ciclo de vida de la cuenta
    protected Timestamp fechaCreacion;    
    protected Timestamp fechaEliminacion; 
    
    // Registro histórico de las comunicaciones enviadas al usuario
    protected List<Notificacion> notificacionesRecibidas; 
}