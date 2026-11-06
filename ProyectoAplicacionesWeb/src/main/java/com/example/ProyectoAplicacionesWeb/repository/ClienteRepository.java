package com.example.ProyectoAplicacionesWeb.repository;

import com.example.ProyectoAplicacionesWeb.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//usamos JpaRepository para consultar y guardar los registros de cliente por su ID
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
