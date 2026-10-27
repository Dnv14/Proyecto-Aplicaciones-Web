package com.example.ProyectoAplicacionesWeb.services;

import com.example.ProyectoAplicacionesWeb.model.Administrador;
import com.example.ProyectoAplicacionesWeb.model.Usuario;
import com.example.ProyectoAplicacionesWeb.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo){
        // Busca por correo, si no existe, informa a spring security del fallo.
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        // Si el objeto es un administrador, asigna admin, en otro caso, cliente.
        String rol = usuario instanceof Administrador ? "ADMIN" : "CLIENTE";
        //user pertenece a spring security, Usuario es nuestra entidad de la base de datos.
        return User.withUsername(usuario.getCorreo()).password(usuario.getContrasenia()).roles(rol).build();
    }
}
