package com.example.ProyectoAplicacionesWeb.services;

import com.example.ProyectoAplicacionesWeb.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;
    
}
