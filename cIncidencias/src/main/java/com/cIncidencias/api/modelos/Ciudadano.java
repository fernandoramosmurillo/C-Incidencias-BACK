package com.cIncidencias.api.modelos;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) // Se encarga de comprobar los datos del padre cuando haga un equals o hash
public class Ciudadano extends Usuario {

    // Datos de contacto y localización específicos del vecino
    private String dni;                // Dni del usuario
    private Integer telefonoContacto;  // Número de teléfono personal
    private String direccion;          // Domicilio del ciudadano
    
    // Historial de interacción con el sistema de incidencias
    private List<Incidencia> incidenciasSolicitadas; // Listado de quejas que ha abierto el ciudadano
    private List<Incidencia> incidenciasCalificadas; // Listado de incidencias en las que el usuario ha dejado valoración
}