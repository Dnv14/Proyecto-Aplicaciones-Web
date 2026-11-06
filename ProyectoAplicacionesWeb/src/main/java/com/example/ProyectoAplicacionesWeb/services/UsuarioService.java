package com.example.ProyectoAplicacionesWeb.services;

import com.example.ProyectoAplicacionesWeb.model.Usuario;
import com.example.ProyectoAplicacionesWeb.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpSession session;

    //registramos al usuario guardando la contraseña como un hash de BCrypt
    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
        return usuarioRepository.save(usuario);
    }

    //buscamos al usuario por correo y comprobamos la contraseña recibida
    public Usuario autenticarUsuario(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            //comparamos la contraseña con el hash guardado, no se necesita descifrarlo
            if (passwordEncoder.matches(password, usuario.getContrasenia())) {
                return usuario;
            }
        }
        return null;
    }

    //buscamos al usuario con el ID guardado en la sesión, si no está disponible devolvemos null
    public Usuario obtenerUsuarioLogeado(){
        //leemos el atributo usuario_id, este método depende de que se haya guardado antes en la sesión
        Long usuarioId = (Long) session.getAttribute("usuario_id");

        if(usuarioId != null){
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
            if(usuarioOpt.isPresent()){
                return usuarioOpt.get();
            }
        }

        return null;
    }

    //revisamos si ya hay un usuario registrado con ese correo
    public boolean existeCorreo(String correo){
        return usuarioRepository.findByCorreo(correo).isPresent();
    }
    
}
