package com.cIncidencias.api.modelos.Serializadores;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.cloud.firestore.GeoPoint;

import java.io.IOException;

public class GeoPointDeserializer extends JsonDeserializer<GeoPoint> {
    
    /**
     * Transforma los datos de latitud y longitud que vienen en el JSON en un objeto 
     * GeoPoint que entiende Firestore. Mapeamos los campos manualmente para 
     * asegurar que las coordenadas se guarden correctamente.
     */
    @Override
    public GeoPoint deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    	
    	//El lector JsonParser convierte el dato en un arbol entero para buscar los datos de una manera mucho mas sencilla
        JsonNode node = p.getCodec().readTree(p);
        double lat = node.get("latitude").asDouble();
        double lon = node.get("longitude").asDouble();
        return new GeoPoint(lat, lon);
    }
}