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

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Categoria findById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
    }

    public List<Categoria> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return findAll();
        }
        return categoriaRepository.findByNombreContainingIgnoreCase(nombre.trim());
    }

    public Categoria crearCategoria(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío.");
        }

        if (categoria.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la categoría no puede quedar vacía.");
        }

        return categoriaRepository.save(categoria);
    }

    public Optional<Categoria> actualizarCategoriaCompleta(Long id, Categoria categoria) {
        return categoriaRepository.findById(id).map(categoriaExistente -> {
            //PUT actualiza el objeto completo
            categoriaExistente.setDescripcion(categoria.getDescripcion());
            categoriaExistente.setNombre(categoria.getNombre());
            categoriaExistente.setProductos(categoria.getProductos());
            return categoriaRepository.save(categoriaExistente);
        });
    }

    public Optional<Categoria> actualizarCategoriaParcial(Long id, Categoria categoria) {

        return categoriaRepository.findById(id).map(categoriaExistente -> {
            if (categoria.getNombre() != null) {
                if (categoria.getNombre().trim().isEmpty()) {
                    throw new IllegalArgumentException("El nombre de la categoría no puede quedar vacío.");
                }
                categoriaExistente.setNombre(categoria.getNombre());
            }

            if (categoria.getDescripcion() != null) {
                if (categoria.getDescripcion().trim().isEmpty()) {
                    throw new IllegalArgumentException("La descripción de la categoría no puede quedar vacía.");
                }
                categoriaExistente.setDescripcion(categoria.getDescripcion());
            }
            if (categoria.getProductos() != null) {
                categoriaExistente.setProductos(categoria.getProductos());
            }
            return categoriaRepository.save(categoriaExistente);
        });
    }

    public void eliminarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede eliminar: La categoría no existe."));

        if (!categoria.getProductos().isEmpty()) {
            throw new RuntimeException("No se puede eliminar: La categoría aún tiene productos.");
        }

        categoriaRepository.deleteById(id);
    }
}
