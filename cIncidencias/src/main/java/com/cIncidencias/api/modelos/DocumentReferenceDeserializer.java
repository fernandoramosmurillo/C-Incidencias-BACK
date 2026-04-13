package com.cIncidencias.api.modelos;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;

public class DocumentReferenceDeserializer extends JsonDeserializer<DocumentReference> {
	
	@Autowired
    private Firestore firestore;
	
	@Override
    public DocumentReference deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		String pathCompleto = p.getText(); // Ejemplo: "usuarios/USR-101"
	    if (pathCompleto == null || !pathCompleto.contains("/") || pathCompleto.isEmpty()) return null;
	    
	    //Crea una referencia al documento en concreto al cual necesitamos
	    return firestore.document(pathCompleto);
    }
}
