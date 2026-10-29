package com.example.ProyectoAplicacionesWeb.services;

import com.example.ProyectoAplicacionesWeb.model.Categoria;
import com.example.ProyectoAplicacionesWeb.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    //ME FALTAN AGREGAR LAS VALIDACIONES DE NEGOCIO
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
            //PATCH actualiza el objeto parcial
            if (categoria.getDescripcion() != null) {
                categoriaExistente.setDescripcion(categoria.getDescripcion());

            }
            if (categoria.getNombre() != null) {
                categoriaExistente.setNombre(categoria.getNombre());
            }
            if (categoria.getProductos() != null) {
                categoriaExistente.setProductos(categoria.getProductos());
            }
            return categoriaRepository.save(categoriaExistente);
        });
    }

    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: La categoría no existe.");
        }
        categoriaRepository.deleteById(id);
    }
}
