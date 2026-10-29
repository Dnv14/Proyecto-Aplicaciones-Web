package com.example.ProyectoAplicacionesWeb.controllers;

import com.example.ProyectoAplicacionesWeb.repository.InventarioRepository;
import com.example.ProyectoAplicacionesWeb.services.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping("/admin/ajustarInventario")
    public String mostrarInventario(){
        return "FramesAdmin/frmAjustarInventario";
    }

}
