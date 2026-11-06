package com.example.ProyectoAplicacionesWeb.services;

import com.example.ProyectoAplicacionesWeb.model.Categoria;
import com.example.ProyectoAplicacionesWeb.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    //obtenemos todas las categorías registradas
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    //buscamos la categoría por ID y avisamos si no existe
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
    }

    //si no hay texto mostramos todas, si hay búsqueda quitamos los espacios de los extremos
    public List<Categoria> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return findAll();
        }
        return categoriaRepository.findByNombreContainingIgnoreCase(nombre.trim());
    }

    //validamos que el nombre y la descripción tengan contenido antes de guardar
    public Categoria crearCategoria(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío.");
        }

        if (categoria.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la categoría no puede quedar vacía.");
        }

        return categoriaRepository.save(categoria);
    }

    //si la categoría existe reemplazamos su descripción, nombre y lista de productos
    public Optional<Categoria> actualizarCategoriaCompleta(Long id, Categoria categoria) {
        return categoriaRepository.findById(id).map(categoriaExistente -> {
            //reemplazamos los tres datos de la categoría con los valores recibidos
            categoriaExistente.setDescripcion(categoria.getDescripcion());
            categoriaExistente.setNombre(categoria.getNombre());
            categoriaExistente.setProductos(categoria.getProductos());
            return categoriaRepository.save(categoriaExistente);
        });
    }

    //actualizamos solo los datos que recibimos, los que vienen nulos se conservan
    public Optional<Categoria> actualizarCategoriaParcial(Long id, Categoria categoria) {

        return categoriaRepository.findById(id).map(categoriaExistente -> {
            //si recibimos un nombre validamos que no esté vacío antes de cambiarlo
            if (categoria.getNombre() != null) {
                if (categoria.getNombre().trim().isEmpty()) {
                    throw new IllegalArgumentException("El nombre de la categoría no puede quedar vacío.");
                }
                categoriaExistente.setNombre(categoria.getNombre());
            }

            //si recibimos una descripción revisamos que tenga contenido
            if (categoria.getDescripcion() != null) {
                if (categoria.getDescripcion().trim().isEmpty()) {
                    throw new IllegalArgumentException("La descripción de la categoría no puede quedar vacía.");
                }
                categoriaExistente.setDescripcion(categoria.getDescripcion());
            }
            //reemplazamos la lista de productos únicamente si fue enviada
            if (categoria.getProductos() != null) {
                categoriaExistente.setProductos(categoria.getProductos());
            }
            return categoriaRepository.save(categoriaExistente);
        });
    }

    //buscamos la categoría antes de intentar eliminarla
    public void eliminarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede eliminar: La categoría no existe."));

        //no permitimos eliminar una categoría que todavía tenga productos asociados
        if (!categoria.getProductos().isEmpty()) {
            throw new RuntimeException("No se puede eliminar: La categoría aún tiene productos.");
        }

        categoriaRepository.deleteById(id);
    }
}
