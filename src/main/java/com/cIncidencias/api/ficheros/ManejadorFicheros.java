package com.cIncidencias.api.ficheros;
import java.io.*;

public class ManejadorFicheros {

    /** Devuelve todo el contenido del archivo en un solo String
     * 
     * @param ruta ruta del archivo
     * @return string con los datos
     */
    public static String leer(String ruta) {
        StringBuilder contenido = new StringBuilder();
        String linea;
        
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            while ((linea = br.readLine()) != null) {
                // Usamos append para ir construyendo el texto
                contenido.append(linea).append("\n");
            }
        } catch (IOException e) {
        	System.err.println("Error en lectura: " + e.getMessage());
            return null;
        }
        
        return contenido.toString(); // Convertimos el constructor a String real
    }

    /** ESCRIBIR: Guarda un String directamente en el archivo usando PrintWriter
     * 
     * @param ruta ruta del archivo
     * @param texto texto que introduciremos
     */
    public static void escribir(String ruta, String texto, boolean sobrescribir) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ruta,!sobrescribir))) {
            
            pw.println(texto);
            
            System.out.println("Archivo guardado con éxito.");
            
        } catch (IOException e) {
            System.out.println("Error al abrir el archivo: " + e.getMessage());
        }
    }
}