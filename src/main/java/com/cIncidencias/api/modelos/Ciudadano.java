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

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Ciudadano extends Usuario {

    private String dni;
    private String telefonoContacto;
    
    // Inicialización directa para evitar NullPointerException
    @JsonSerialize(contentUsing = DocumentReferenceSerializer.class)
    @JsonDeserialize(contentUsing = DocumentReferenceDeserializer.class)
    private List<DocumentReference> incidenciasSolicitadas = new ArrayList<>(); 

    @JsonSerialize(contentUsing = DocumentReferenceSerializer.class)
    @JsonDeserialize(contentUsing = DocumentReferenceDeserializer.class)
    private List<DocumentReference> incidenciasCalificadas = new ArrayList<>();
}