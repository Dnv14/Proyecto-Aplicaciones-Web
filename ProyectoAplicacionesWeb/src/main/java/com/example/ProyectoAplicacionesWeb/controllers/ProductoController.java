package com.example.ProyectoAplicacionesWeb.controllers;

import com.example.ProyectoAplicacionesWeb.model.Producto;
import com.example.ProyectoAplicacionesWeb.repository.CategoriaRepository;
import com.example.ProyectoAplicacionesWeb.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    public String mostrarProductos(@RequestParam(name = "buscar", required = false) String buscar,Model model) {
        if (buscar == null || buscar.isBlank()) {
            model.addAttribute("productos",productoService.obtenerProductos());
        }else if(buscar.matches("\\d+")){
            model.addAttribute("productos",productoService.buscarProductoPorId(Long.valueOf(buscar)));
        }else{
            model.addAttribute("productos",productoService.buscarProductosPorNombre(buscar));
        }
        model.addAttribute("busquedaActual",buscar);
        return "FramesAdmin/frmInventario";
    }


    @PostMapping("/admin/crearProducto")
    public String crearProducto(@ModelAttribute Producto producto,
                                @RequestParam("imagenArchivo") MultipartFile imagenArchivo) throws IOException {
        productoService.guardarProducto(producto, imagenArchivo);
        return "redirect:/admin/productos";
    }

    @PostMapping("/admin/productos/{id}/estado")
    public String cambiarEstadoProducto(@PathVariable Long id){
        Producto producto=productoService.obtenerProductoPorId(id).orElseThrow(()->new RuntimeException("Producto no encontrado"));
        productoService.cambiarEstadoProducto(id,!producto.getActivo());
        return "redirect:/admin/productos";
    }

    @GetMapping("/admin/productos/{id}/editar")
    public String mostrarEditarProducto(@PathVariable Long id, Model model) {
        Producto producto=productoService.obtenerProductoPorId(id).orElseThrow(()->new RuntimeException("Producto no encontrado"));
        model.addAttribute("producto",producto);
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "FramesAdmin/frmEditarProducto";
    }

    @PostMapping("/admin/productos/{id}/editar")
    public String actualizarProducto(@PathVariable Long id,
                                     @ModelAttribute Producto producto,
                                     @RequestParam("imagenArchivo") MultipartFile imagenArchivo) throws IOException {
        productoService.actualizarProducto(id, producto, imagenArchivo);
        return"redirect:/admin/productos";
    }

}
