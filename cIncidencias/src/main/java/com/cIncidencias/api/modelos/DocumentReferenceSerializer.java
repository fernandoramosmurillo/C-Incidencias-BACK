package com.cIncidencias.api.modelos;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.cloud.firestore.DocumentReference;

import java.io.IOException;

public class DocumentReferenceSerializer extends JsonSerializer<DocumentReference> {
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