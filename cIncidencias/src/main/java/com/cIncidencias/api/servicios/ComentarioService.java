package com.cIncidencias.api.servicios;

import com.cIncidencias.api.modelos.Comentario;
import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.repositorios.ComentarioRepository;
import com.cIncidencias.api.excepciones.NullParamsException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ComentarioService implements IGenericoService<Comentario> {

    private final ComentarioRepository comentarioRepository;

    public ComentarioService(ComentarioRepository comentarioRepository) {
        this.comentarioRepository = comentarioRepository;
    }

    @Override
    public void guardar(Comentario comentario) throws NullParamsException, ExecutionException, InterruptedException, IOException {
        // Validación básica de objeto y contenido
        if (comentario == null) {
            throw new NullParamsException("Se necesita un comentario para publicarlo");
        }
        
        if (comentario.getTexto() == null || comentario.getTexto().trim().isEmpty()) {
            throw new NullParamsException("El texto del comentario no puede estar vacío.");
        }
        
        comentarioRepository.guardar(comentario);
    }

    @Override
    public List<Comentario> obtenerTodos() throws ExecutionException, InterruptedException {
        return comentarioRepository.obtenerTodos();
    }

    @Override
    public Comentario obtenerPorId(String idComentario) throws NullParamsException, ExecutionException, InterruptedException {
        // Evitamos buscar si el ID viene vacío o con espacios
        if (idComentario == null || idComentario.trim().isEmpty()) {
            throw new NullParamsException("Es necesario un ID válido para buscar el comentario.");
        }
        return comentarioRepository.obtenerPorId(idComentario);
    }

    @Override
    public void eliminar(String idComentario) throws NullParamsException, ExecutionException, InterruptedException, IOException {
        // El ID es obligatorio para el borrado en Firestore
        if (idComentario == null || idComentario.trim().isEmpty()) {
            throw new NullParamsException("Es necesario un ID válido para eliminar el comentario.");
        }
        comentarioRepository.eliminar(idComentario);
    }

    @Override
    public void modificar(Comentario comentario) throws NullParamsException, ExecutionException, InterruptedException, IOException {
        // Validamos que el objeto y su ID existan antes de actualizar
        if (comentario == null || comentario.getIdComentario() == null || comentario.getIdComentario().trim().isEmpty()) {
            throw new NullParamsException("Datos insuficientes para modificar el comentario (ID ausente).");
        }
        
        // El texto nuevo también debe tener contenido real
        if (comentario.getTexto() == null || comentario.getTexto().trim().isEmpty()) {
            throw new NullParamsException("El nuevo texto del comentario no puede estar vacío.");
        }

        comentarioRepository.modificar(comentario);
    }
    
    @Override
    public void cambiarEstado(String idComentario, ModeloBase.Estados estado) throws Exception {

        if (idComentario == null || idComentario.trim().isEmpty()) {
            throw new NullParamsException("Se necesita un ID válido para eliminar el comentario temporalmente.");
        }

        comentarioRepository.cambiarEstado(idComentario, estado);
    }
}