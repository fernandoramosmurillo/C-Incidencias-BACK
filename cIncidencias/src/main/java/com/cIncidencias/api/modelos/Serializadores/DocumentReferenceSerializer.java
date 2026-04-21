package com.cIncidencias.api.modelos.Serializadores;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.cloud.firestore.DocumentReference;

import java.io.IOException;

public class DocumentReferenceSerializer extends JsonSerializer<DocumentReference> {
    
    /**
     * Convierte la referencia de Firestore en una cadena de texto plana para el JSON.
     * Básicamente, en vez de mandar todo el objeto de la referencia (que es un lío), 
     * mandamos solo la ruta tipo "coleccion/id" para que sea más fácil de leer fuera.
     */
    @Override
    public void serialize(DocumentReference value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            // Esto hará que en el JSON solo aparezca "usuarios/USR-000000001"
            gen.writeString(value.getPath());
        } else {
            gen.writeNull();
        }
    }
}