package com.cIncidencias.api.modelos;

import java.util.List;
import com.google.cloud.Timestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Diferentes áreas que puede gestionar un administrador en el ayuntamiento
enum Departamentos {
    D_ELECTRICIDAD, D_FONTANERIA, D_OBRAS_PUBLICAS, D_JARDINERIA,
    D_LIMPIEZA_VIARIA, D_CARPINTERIA, D_PINTURA, D_CERRAJERIA, D_ALBAÑILERIA
}

@Data
@EqualsAndHashCode(callSuper = true) // Comprueba los datos del padre (Usuario) al hacer comparaciones
public class Administrador extends Usuario {

    // Controles de seguridad y niveles de gestión
    private Integer nivelAcceso;           // Rango de poder dentro del panel de control
    private Integer intentosPushDenegados; // Registro de seguridad sobre login fallidos
    private Boolean esSuperAdmin;          // Indica si el administrador es un administrador global, es decir, puede eliminar otros administradores y esta excepto de credenciales
    private String firmaDigitalId;         // Identificador para validar documentos u órdenes oficiales
    
    private Departamentos departamento;    // El área específica asignada al administrador
    
    private Timestamp fechaCambioClave;    
    
    private String claveAdmin;             // Contraseña especial para las administradores
    
    // Historial de noticias o bandos publicados por este administrador
    private List<Noticia> listaNoticiasEnviadas; 
}