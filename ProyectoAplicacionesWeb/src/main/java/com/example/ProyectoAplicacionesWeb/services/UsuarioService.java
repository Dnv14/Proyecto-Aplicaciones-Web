package com.example.ProyectoAplicacionesWeb.services;

import com.example.ProyectoAplicacionesWeb.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;


}
