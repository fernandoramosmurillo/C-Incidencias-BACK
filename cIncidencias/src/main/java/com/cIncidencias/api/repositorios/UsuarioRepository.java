package com.cIncidencias.api.repositorios;

import com.cIncidencias.api.modelos.Usuario;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Esta clase es el "puente" entre las clase de java y firebase
 * 
 * Se crea la conexion cuanto al documento y coleccion que pertenece a los usuarios
 */
@Repository
public class UsuarioRepository {

    private final Firestore firestore;

    // Spring inyecta automáticamente la conexión configurada en FirebaseConfig
    public UsuarioRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public String guardarUsuario(Usuario usuario) throws ExecutionException, InterruptedException {
    	
        // Creamos el documento (filas y tabla), si ya existe, entramos en el
    	DocumentReference docRef = firestore.collection("usuarios").document(usuario.getIdUsuario());
        
        ApiFuture<WriteResult> result = docRef.set(usuario);
        
        // Hacemos un log de la confirmación junto a la hora en la que se guardó en Google Cloud
        return "Usuario"+usuario.getNombre()+ " "+usuar con éxito en Firestore. Fecha: " + result.get().getUpdateTime();
    }
    
    public List<Usuario> obtenerUsuarios() throws InterruptedException, ExecutionException {
    	ApiFuture<QuerySnapshot> query = firestore.collection("usuarios").get();
    	QuerySnapshot querySnapshot = query.get();
    	return querySnapshot.toObjects(Usuario.class);
    }
    
    public Usuario obtenerUsuario(String idUsuario) throws InterruptedException, ExecutionException {
    	DocumentReference docRef = firestore.collection("usuarios").document(idUsuario);
    	ApiFuture<DocumentSnapshot> docSnapshot = docRef.get();
    	return docSnapshot.get().toObject(Usuario.class);
    }
    
    public void eliminarUsuario(String idUsuario) throws InterruptedException, ExecutionException {
    	DocumentReference docRef = firestore.collection("usuarios").document(idUsuario);
    	
    	ApiFuture<WriteResult> result = docRef.delete();
    	
    }
    
    
    
}