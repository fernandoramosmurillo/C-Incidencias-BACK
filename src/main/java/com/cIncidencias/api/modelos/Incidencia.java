package com.cIncidencias.api.modelos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.cIncidencias.api.modelos.Serializadores.DocumentReferenceDeserializer;
import com.cIncidencias.api.modelos.Serializadores.DocumentReferenceSerializer;
import com.cIncidencias.api.modelos.Serializadores.GeoPointDeserializer;
import com.cIncidencias.api.modelos.Serializadores.GeoPointSerializer;
import com.cIncidencias.api.modelos.Serializadores.TimestampDeserializer;
import com.cIncidencias.api.modelos.Serializadores.TimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.GeoPoint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Representa una incidencia en el sistema. Hereda el estado de persistencia de
 * ModeloBase y gestiona su propio flujo de trabajo.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Incidencia extends ModeloBase {

	/**
	 * Define en qué punto de la resolución se encuentra la tarea (si está recién
	 * abierta, asignada, etc).
	 */
	public enum EstadosIncidencia {
		ABIERTA, ASIGNADA, PENDIENTE, SOLUCIONADA, RECHAZADA
	}

	/** Niveles de urgencia que ayudan a priorizar la atención de la incidencia. */
	public enum Prioridades {
		MINIMO, BAJA, MEDIA, ALTA, MUY_ALTA, URGENTE
	}

	public enum Categorias {
		ALUMBRADO_PUBLICO, VIA_PUBLICA, LIMPIEZA_Y_RESIDUOS, PARQUES_Y_JARDINES, SISTEMAS_DESAGUE, SISTEMA_TUBERIAS,
		SEÑALES_Y_TRAFICO, MOBILIARIO_URBANO, ANIMALES_Y_PLAGAS, OBRAS_Y_ESCOMBROS
	}

	private String idIncidencia;
	private String titulo;
	private String descripcion;
	public List<String> categorias;

	@JsonSerialize(using = GeoPointSerializer.class)
	@JsonDeserialize(using = GeoPointDeserializer.class)
	private GeoPoint ubicacion; // Punto geográfico exacto para localizar el problema en el mapa

	private List<String> imagenesUrl; // Enlaces a la fotos que el ciudadano sube como evidencia

	@JsonSerialize(using = TimestampSerializer.class)
	@JsonDeserialize(using = TimestampDeserializer.class)
	private Timestamp fechaCreacion;

	@JsonSerialize(using = TimestampSerializer.class)
	@JsonDeserialize(using = TimestampDeserializer.class)
	private Timestamp fechaCierre; // Marca de tiempo para cuando se da por finalizada la gestión

	@JsonDeserialize(using = DocumentReferenceDeserializer.class)
	@JsonSerialize(using = DocumentReferenceSerializer.class)
	private DocumentReference usuarioCiudadano; // Referencia directa al perfil del creador en Firestore

	@JsonSerialize(contentUsing = DocumentReferenceSerializer.class)
	@JsonDeserialize(contentUsing = DocumentReferenceDeserializer.class)
	private List<DocumentReference> listaOperarios = new ArrayList<>(); // Diccionario de técnicos asignados para evitar
																		// nulos

	@JsonSerialize(contentUsing = DocumentReferenceSerializer.class)
	@JsonDeserialize(contentUsing = DocumentReferenceDeserializer.class)
	private List<DocumentReference> comentarios = new ArrayList<>(); // Listado de comentarios para mantener el
																		// historial de charla

	// Atributos de categorización propios de la incidencia
	private Prioridades prioridad;
	private EstadosIncidencia estadoIncidencia = EstadosIncidencia.ABIERTA; // Estado inicial por defecto

	@JsonDeserialize(using = DocumentReferenceDeserializer.class)
	@JsonSerialize(using = DocumentReferenceSerializer.class)
	private DocumentReference valoracion;
}