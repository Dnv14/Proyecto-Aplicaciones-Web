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

    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
        return usuarioRepository.save(usuario);
    }

    public Usuario autenticarUsuario(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(password, usuario.getContrasenia())) {
                return usuario;
            }
        }
        return null;
    }

    public Usuario obtenerUsuarioLogeado(){
        Long usuarioId = (Long) session.getAttribute("usuario_id");

        if(usuarioId != null){
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
            if(usuarioOpt.isPresent()){
                return usuarioOpt.get();
            }
        }

        return null;
    }

    public boolean existeCorreo(String correo){
        return usuarioRepository.findByCorreo(correo).isPresent();
    }
    
}
