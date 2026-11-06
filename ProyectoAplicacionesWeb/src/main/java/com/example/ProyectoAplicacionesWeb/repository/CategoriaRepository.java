package com.example.ProyectoAplicacionesWeb.repository;

import com.example.ProyectoAplicacionesWeb.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//usamos JpaRepository para consultar y guardar los registros de categoria por su ID
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    //buscamos categorías que contengan el texto sin distinguir mayúsculas de minúsculas
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
}
