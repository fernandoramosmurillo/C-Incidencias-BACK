package com.cIncidencias.api.modelos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor

public class AuthUsuario {
	private String uid;
    private String email;
    private boolean verificado;
    private String nombre;
    private String foto;
}
