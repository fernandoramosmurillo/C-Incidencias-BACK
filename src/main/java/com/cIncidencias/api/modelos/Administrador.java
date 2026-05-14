package com.cIncidencias.api.modelos;

import java.util.ArrayList;
import java.util.List;

import com.cIncidencias.api.modelos.Serializadores.DocumentReferenceDeserializer;
import com.cIncidencias.api.modelos.Serializadores.DocumentReferenceSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.cloud.firestore.DocumentReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// Diferentes áreas que puede gestionar un administrador en el ayuntamiento
enum Departamentos {
	D_ELECTRICIDAD, D_FONTANERIA, D_OBRAS_PUBLICAS, D_JARDINERIA, D_LIMPIEZA_VIARIA, D_CARPINTERIA, D_PINTURA,
	D_CERRAJERIA, D_ALBAÑILERIA
}

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true) // Comprueba los datos del padre (Usuario) al hacer comparaciones
public class Administrador extends Ciudadano {

	// Controles de seguridad y niveles de gestión
	private Integer nivelAcceso; // Rango de poder dentro del panel de control
	private Boolean esSuperAdmin; // Indica si el administrador es un administrador global, es decir, puede									// eliminar otros administradores y esta excepto de credenciales
	private Departamentos departamento; // El área específica asignada al administrador

	// Historial de noticias o bandos publicados por este administrador
	// Inicializamos con ArrayList para evitar NullPointerException en el resto de la app
	@JsonSerialize(contentUsing = DocumentReferenceSerializer.class)
	@JsonDeserialize(contentUsing = DocumentReferenceDeserializer.class)
	private List<DocumentReference> listaNoticiasEnviadas = new ArrayList<>();
}