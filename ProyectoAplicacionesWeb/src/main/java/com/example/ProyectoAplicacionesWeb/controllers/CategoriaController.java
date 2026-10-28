package com.example.ProyectoAplicacionesWeb.controllers;

import com.example.ProyectoAplicacionesWeb.model.Categoria;

import com.example.ProyectoAplicacionesWeb.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/admin/gestionarCategorias")
    public String mostrarCategorias(Model model) {
        model.addAttribute("categorias", categoriaService.findAll());
        return "FramesAdmin/frmGestionarCategorias";
    }

    @GetMapping("/admin/crearCategoria")
    public String crearCategoria(@ModelAttribute Categoria categoria) {
        categoriaService.crearCategoria(categoria);
        return "redirect:/admin/gestionCategorias";
    }


}
