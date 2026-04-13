package com.cIncidencias.api.modelos.Serializadores;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.cloud.firestore.GeoPoint;

public class GeoPointSerializer extends JsonSerializer<GeoPoint> {

    @Override
    public void serialize(GeoPoint value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            // Iniciamos la escritura del objeto JSON {
            gen.writeStartObject();
            
            // Escribimos cada campo con su nombre y su valor
            gen.writeNumberField("latitude", value.getLatitude());
            gen.writeNumberField("longitude", value.getLongitude());
            
            // Cerramos el objeto }
            gen.writeEndObject();
        } else {
            gen.writeNull();
        }
    }
}