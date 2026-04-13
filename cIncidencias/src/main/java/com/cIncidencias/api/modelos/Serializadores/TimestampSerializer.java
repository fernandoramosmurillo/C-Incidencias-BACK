package com.cIncidencias.api.modelos.Serializadores;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.cloud.Timestamp;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class TimestampSerializer extends JsonSerializer<Timestamp> {

    @Override
    public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            // Convertimos el Timestamp de Google a un String
            // Usamos la 'Z' literal para indicar UTC, tal como se espera en el POST
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            
            String fechaFormateada = sdf.format(value.toDate());
            gen.writeString(fechaFormateada);
        } else {
            gen.writeNull();
        }
    }
} 