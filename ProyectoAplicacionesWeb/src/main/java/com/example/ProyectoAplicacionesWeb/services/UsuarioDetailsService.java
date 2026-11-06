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
    //Spring Security llama a este método usando el correo recibido en el login
    public UserDetails loadUserByUsername(String correo){
        //buscamos por correo y avisamos a Spring Security si el usuario no existe
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        //si el usuario es un administrador asignamos ADMIN, en otro caso asignamos CLIENTE
        String rol = usuario instanceof Administrador ? "ADMIN" : "CLIENTE";
        //creamos el User de Spring Security usando el correo, el hash de la contraseña y el rol de nuestro usuario
        return User.withUsername(usuario.getCorreo()).password(usuario.getContrasenia()).roles(rol).build();
    }
}
