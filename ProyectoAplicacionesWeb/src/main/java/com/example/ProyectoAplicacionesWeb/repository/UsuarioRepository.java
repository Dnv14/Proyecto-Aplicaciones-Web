package com.example.ProyectoAplicacionesWeb.repository;

import com.example.ProyectoAplicacionesWeb.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//usamos JpaRepository para consultar y guardar los registros de usuario por su ID
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //buscamos al usuario por su correo, el resultado puede estar vacío
    Optional<Usuario> findByCorreo(String correo);
}
