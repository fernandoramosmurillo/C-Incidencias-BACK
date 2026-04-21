package com.cIncidencias.api.modelos.Serializadores;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.cloud.firestore.GeoPoint;

public class GeoPointSerializer extends JsonSerializer<GeoPoint> {

	/**
	 * Convierte un GeoPoint de Firestore en un objeto JSON con sus campos de latitud 
	 * y longitud por separado. Lo hacemos así para que el frontend pueda leer 
	 * las coordenadas fácilmente sin pelearse con el formato interno de Google.
	 */
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