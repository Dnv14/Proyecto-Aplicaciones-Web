package com.example.ProyectoAplicacionesWeb.controllers;

import com.example.ProyectoAplicacionesWeb.model.Producto;
import com.example.ProyectoAplicacionesWeb.repository.CategoriaRepository;
import com.example.ProyectoAplicacionesWeb.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping("/admin/crearProducto")
    public String mostrarCrearProducto(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "FramesAdmin/frmCrearProducto";
    }

    @GetMapping("/admin/productos")
    public String mostrarProductos(Model model) {
        model.addAttribute("productos",productoService.obtenerProductos());
        return "FramesAdmin/frmInventario";
    }


    @PostMapping("/admin/crearProducto")
    public String crearProducto(@ModelAttribute Producto producto,
                                @RequestParam("imagenArchivo") MultipartFile imagenArchivo) throws IOException {
        productoService.guardarProducto(producto, imagenArchivo);
        return "redirect:/admin/productos";
    }

}
