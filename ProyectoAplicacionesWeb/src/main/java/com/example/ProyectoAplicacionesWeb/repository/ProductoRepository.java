package com.example.ProyectoAplicacionesWeb.repository;

import com.example.ProyectoAplicacionesWeb.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoria_Nombre(String nombre);

}
