package com.cIncidencias.api.modelos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo que unifica los datos de usuario y los de authenticator
 */
@Data
@NoArgsConstructor
@AllArgsConstructor // La añadimos para poder hacer el "new FullUser(u, a)" fácilmente
public class FullUsuario {
    
    private Usuario datosusuario;
    private AuthUsuario datosAuth;

}