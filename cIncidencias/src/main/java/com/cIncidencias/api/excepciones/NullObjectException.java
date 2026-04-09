package com.cIncidencias.api.excepciones;

public class NullObjectException extends Exception{

	public NullObjectException() {
		super();
	}
	
	public NullObjectException(String mensaje) {
		super(mensaje);
	}
	
}
