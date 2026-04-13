package com.cIncidencias.api.modelos.Serializadores;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.cloud.Timestamp;

public class TimestampDeserializer extends JsonDeserializer<Timestamp> {
	
	@Override
    public Timestamp deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // Le explicamos a Jackson: "Lee el texto y pásalo por el filtro de Google"
        String valorJson = p.getText(); 
        return Timestamp.parseTimestamp(valorJson);
    }
}
