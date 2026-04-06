package com.cIncidencias.api.modelos;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data // Getters y Setters automáticos para mantener el código limpio y ordenado
public class Comentario {

    private String idComentario;      // El identificador único de cada mensaje
    private String texto;             // El contenido del mensaje (lo que escribe el usuario)
    
    // Al igual que en el resto del proyecto, usamos el tiempo oficial de Google Cloud
    private Timestamp fechaPublicacion; 
    
    // Indica si el comentario es solo para operarios/admin o si es público para el ciudadano
    private Boolean esPrivado;        
    
    // El objeto Usuario que escribió el mensaje (Ciudadano, Operario o Admin)
    private Usuario usuarioAutor;     
}