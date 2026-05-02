package com.cIncidencias.api.servicios;

import org.springframework.stereotype.Service;

import com.cIncidencias.api.modelos.AuthUsuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

@Service
public class FirebaseAuthService {

    public AuthUsuario verificarToken(String bearerToken) throws FirebaseAuthException {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token no proporcionado o formato inválido");
        }

        String idToken = bearerToken.replace("Bearer ", "");
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);

        return new AuthUsuario(
            decodedToken.getUid(),
            decodedToken.getEmail(),
            decodedToken.isEmailVerified(),
            decodedToken.getName(),
            decodedToken.getPicture()
        );
    }
}