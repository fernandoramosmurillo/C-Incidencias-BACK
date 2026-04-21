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

    /**
     * Valida y guarda un comentario.
     * Se asegura de que el comentario no sea nulo y que contenga un texto real 
     * antes de enviarlo a Firestore.
     */
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

    /**
     * Obtiene todos los comentarios registrados.
     */
    @Override
    public List<Comentario> obtenerTodos() throws ExecutionException, InterruptedException {
        return comentarioRepository.obtenerTodos();
    }

    /**
     * Busca un comentario por ID, validando que el parámetro de búsqueda sea coherente.
     */
    @Override
    public Comentario obtenerPorId(String idComentario) throws NullParamsException, ExecutionException, InterruptedException {
        // Evitamos buscar si el ID viene vacío o con espacios
        if (idComentario == null || idComentario.trim().isEmpty()) {
            throw new NullParamsException("Es necesario un ID válido para buscar el comentario.");
        }
        return comentarioRepository.obtenerPorId(idComentario);
    }

    /**
     * Procesa la eliminación física de un comentario tras validar su identificador.
     */
    @Override
    public void eliminar(String idComentario) throws NullParamsException, ExecutionException, InterruptedException, IOException {
        // El ID es obligatorio para el borrado en Firestore
        if (idComentario == null || idComentario.trim().isEmpty()) {
            throw new NullParamsException("Es necesario un ID válido para eliminar el comentario.");
        }
        comentarioRepository.eliminar(idComentario);
    }

    /**
     * Gestiona la actualización de un comentario existente.
     * Verifica que el comentario tenga un ID asignado y que el nuevo contenido 
     * cumpla con los requisitos mínimos.
     */
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
    
    /**
     * Cambia el estado (borrado lógico, bloqueo, etc.) del comentario.
     * Valida la presencia del ID antes de realizar la operación en la base de datos.
     */
    @Override
    public void cambiarEstado(String idComentario, ModeloBase.Estados estado) throws Exception {

        if (idComentario == null || idComentario.trim().isEmpty()) {
            throw new NullParamsException("Se necesita un ID válido para cambiar el estado del comentario.");
        }

        comentarioRepository.cambiarEstado(idComentario, estado);
    }
}