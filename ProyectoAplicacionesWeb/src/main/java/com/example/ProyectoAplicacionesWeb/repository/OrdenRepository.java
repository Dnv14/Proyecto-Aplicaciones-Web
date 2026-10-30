package com.example.ProyectoAplicacionesWeb.repository;

import com.example.ProyectoAplicacionesWeb.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findAll();
    Optional<Orden> findById(Long id);
}
