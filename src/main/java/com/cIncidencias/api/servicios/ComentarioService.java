package com.cIncidencias.api.servicios;

import com.cIncidencias.api.modelos.Comentario;
import com.cIncidencias.api.modelos.ModeloBase;
import com.cIncidencias.api.repositorios.ComentarioRepository;
import com.cIncidencias.api.excepciones.NullParamsException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Servicio encargado de gestionar la lógica de negocio de los comentarios.
 * Actúa como filtro de seguridad y validación antes de persistir los datos en el repositorio.
 */
@Service
public class ComentarioService implements IGenericoService<Comentario> {

    private final ComentarioRepository comentarioRepository;

    public ComentarioService(ComentarioRepository comentarioRepository) {
        this.comentarioRepository = comentarioRepository;
    }

    @Override
    public void guardar(Comentario comentario) throws NullParamsException, ExecutionException, InterruptedException, IOException {
        if (comentario == null || comentario.getIdComentario() == null) {
            throw new NullParamsException("Datos insuficientes para guardar el comentario.");
        }
        
        // Comprobamos si ya existe para no sobrescribir accidentalmente
        if (comentarioRepository.existePorId(comentario.getIdComentario())) {
            throw new NullParamsException("El comentario con ID " + comentario.getIdComentario() + " ya existe.");
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
        if (idComentario == null || idComentario.trim().isEmpty()) {
            throw new NullParamsException("Es necesario un ID válido.");
        }
        return comentarioRepository.obtenerPorId(idComentario);
    }

    @Override
    public void eliminar(String idComentario) throws NullParamsException, ExecutionException, InterruptedException, IOException {
        if (idComentario == null || idComentario.trim().isEmpty()) {
            throw new NullParamsException("ID no válido para eliminar.");
        }

        // Validación de existencia antes de proceder al borrado
        if (!comentarioRepository.existePorId(idComentario)) {
            throw new NullParamsException("No se puede eliminar: el comentario no existe.");
        }

        comentarioRepository.eliminar(idComentario);
    }

    @Override
    public void modificar(Comentario comentario) throws NullParamsException, ExecutionException, InterruptedException, IOException {
        if (comentario == null || comentario.getIdComentario() == null || comentario.getIdComentario().trim().isEmpty()) {
            throw new NullParamsException("ID ausente para modificar.");
        }

        // Validación crítica: ¿Existe lo que queremos modificar?
        if (!comentarioRepository.existePorId(comentario.getIdComentario())) {
            throw new NullParamsException("Imposible modificar: el comentario no existe en la base de datos.");
        }
        
        if (comentario.getTexto() == null || comentario.getTexto().trim().isEmpty()) {
            throw new NullParamsException("El nuevo texto no puede estar vacío.");
        }

        comentarioRepository.modificar(comentario);
    }
    
    @Override
    public void cambiarEstado(String idComentario, ModeloBase.Estados estado) throws Exception {
        if (idComentario == null || idComentario.trim().isEmpty()) {
            throw new NullParamsException("ID no válido para cambiar estado.");
        }

        // Verificamos existencia antes de actualizar el campo en Firestore
        if (!comentarioRepository.existePorId(idComentario)) {
            throw new NullParamsException("No se encontró el comentario para actualizar su estado.");
        }

        comentarioRepository.cambiarEstado(idComentario, estado);
    }
}