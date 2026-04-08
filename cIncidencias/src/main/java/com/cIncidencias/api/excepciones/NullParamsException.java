package com.cIncidencias.api.excepciones;

public class NullParamsException extends Exception{

	public NullParamsException() {
		super();
	}
	
	public NullParamsException(String mensaje) {
		super(mensaje);
	}
	
}
