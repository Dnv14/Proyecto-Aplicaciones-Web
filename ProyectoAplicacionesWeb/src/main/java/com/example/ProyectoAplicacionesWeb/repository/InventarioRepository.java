package com.example.ProyectoAplicacionesWeb.repository;

import com.example.ProyectoAplicacionesWeb.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//usamos JpaRepository para consultar y guardar los registros de inventario por su ID
public interface InventarioRepository extends JpaRepository<Inventario, Long> {



}
