package com.example.ProyectoAplicacionesWeb.controllers;

import com.example.ProyectoAplicacionesWeb.repository.InventarioRepository;
import com.example.ProyectoAplicacionesWeb.repository.ProductoRepository;
import com.example.ProyectoAplicacionesWeb.services.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping("/admin/ajustarInventario")
    public String mostrarInventario(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        return "FramesAdmin/frmAjustarInventario";
    }



}
