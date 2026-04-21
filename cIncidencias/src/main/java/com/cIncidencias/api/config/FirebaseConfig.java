package com.cIncidencias.api.config;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

@Configuration
public class FirebaseConfig {
	
	/**
	 * Configura el SDK de Firebase al arrancar. He puesto un control para que no 
	 * intente inicializar la App si ya existe una instancia, que si no da errores 
	 * raros al recargar en caliente con Spring.
	 */
	@PostConstruct
    public void initFirebase() {
        try {
            // Buscamos el archivo JSON en la carpeta de recursos
            FileInputStream serviceAccount = 
                new FileInputStream("src/main/resources/serviceAccountKey.json");

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

            // Evitamos duplicar la conexión si se reinicia el servidor
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
            
            System.out.println("--------------------------------------------");
            System.out.println(">>> FIREBASE CONECTADO CORRECTAMENTE 🔥 <<<");
            System.out.println("--------------------------------------------");
            
        } catch (IOException e) {
            System.err.println("ERROR CRÍTICO AL CONECTAR FIREBASE: " + e.getMessage());
        }
    }

	/**
	 * Crea el punto de acceso a Firestore. Lo dejo como Bean para poder inyectarlo 
	 * directamente en cualquier servicio y no andar repitiendo código.
	 * * @return Instancia de Firestore para trabajar con la base de datos.
	 */
    @Bean
    public Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }
}