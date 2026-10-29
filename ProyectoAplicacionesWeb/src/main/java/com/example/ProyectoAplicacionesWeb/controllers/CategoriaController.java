package com.example.ProyectoAplicacionesWeb.controllers;

import com.example.ProyectoAplicacionesWeb.model.Categoria;

import com.example.ProyectoAplicacionesWeb.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    //LOS GET

    @GetMapping("/admin/categorias")
    public String mostrarCategorias(@RequestParam(name = "buscar", required = false) String buscar, Model model) {
        if (buscar != null && !buscar.isBlank()) {
            model.addAttribute("categorias", categoriaService.buscarPorNombre(buscar));
        } else {
            model.addAttribute("categorias", categoriaService.findAll());
        }
        return "FramesAdmin/frmGestionarCategorias";
    }

    @GetMapping("/admin/crearCategoria")
    public String mostrarCrearCategoria(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "FramesAdmin/frmAgregarCategoria";
    }

    @GetMapping("/admin/editarCategoria/{id}")
    public String mostrarEditarCategoria(@PathVariable("id") Long id, Model model) {
        model.addAttribute("categoria", categoriaService.findById(id));
        return "FramesAdmin/frmEditarCategoria";
    }

    //LOS POST

    @PostMapping("/admin/crearCategoria")
    public String procesarCrearCategoria(@ModelAttribute Categoria categoria) {
        categoriaService.crearCategoria(categoria);
        return "redirect:/admin/categorias";
    }

    @PostMapping("/admin/editarCategoria")
    public String procesarEditarCategoria(@ModelAttribute Categoria categoria) {
        categoriaService.actualizarCategoriaParcial(categoria.getIdCategoria(), categoria);
        return "redirect:/admin/categorias";
    }


    @PostMapping("/admin/eliminarCategoria/{id}")
    public String eliminarCategoria(@PathVariable("id") Long id) {
        categoriaService.eliminarCategoria(id);
        return "redirect:/admin/categorias";
    }
}
