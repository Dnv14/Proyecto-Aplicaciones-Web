package com.example.ProyectoAplicacionesWeb.repository;

import com.example.ProyectoAplicacionesWeb.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Ordenrepository extends JpaRepository<Orden, Long> {
}
