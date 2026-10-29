package com.example.ProyectoAplicacionesWeb.controllers;

import com.example.ProyectoAplicacionesWeb.repository.InventarioRepository;
import com.example.ProyectoAplicacionesWeb.services.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

}
