package com.example.ProyectoAplicacionesWeb.repository;

import com.example.ProyectoAplicacionesWeb.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//usamos JpaRepository para consultar y guardar los registros de administrador por su ID
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

}
