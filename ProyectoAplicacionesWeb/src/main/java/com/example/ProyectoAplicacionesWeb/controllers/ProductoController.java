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
    //este metodo muestra el inventario por nombre del producto o id, obtenemos el parametro de la URL y el modelo que nos permite inviar datos
    public String mostrarProductos(@RequestParam(name = "buscar", required = false) String buscar, Model model) {
        //validamos si no busco algo o viene vacio
        if (buscar == null || buscar.isBlank()) {
            //mostramos todos los productos registrados, porque no busco nada
            model.addAttribute("productos", productoService.obtenerProductos());
            //validamos si no contiene un digito numerico
        } else if (buscar.matches("\\d+")) {
            //si encuenta busca por id
            model.addAttribute("productos", productoService.buscarProductoPorId(Long.valueOf(buscar)));
        } else {
            //si no se cumple ninguna de las dos anteriores quiere buscamos por nombre
            model.addAttribute("productos", productoService.buscarProductosPorNombre(buscar));
        }
        //enviamos lo buscado al html
        model.addAttribute("busquedaActual", buscar);
        //indico lo que se debe mostrar en la vista
        return "FramesAdmin/frmInventario";
    }


    @PostMapping("/admin/crearProducto")
    public String crearProducto(@ModelAttribute Producto producto,
                                @RequestParam("imagenArchivo") MultipartFile imagenArchivo, Model model) throws IOException {
        try {
            productoService.guardarProducto(producto, imagenArchivo);
            return "redirect:/admin/productos";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "FramesAdmin/frmCrearProducto";
        }
    }

    @PostMapping("/admin/productos/{id}/estado")
    public String cambiarEstadoProducto(@PathVariable Long id) {
        Producto producto = productoService.obtenerProductoPorId(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        productoService.cambiarEstadoProducto(id, !producto.getActivo());
        return "redirect:/admin/productos";
    }

    @GetMapping("/admin/productos/{id}/editar")
    public String mostrarEditarProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.obtenerProductoPorId(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "FramesAdmin/frmEditarProducto";
    }

    @PostMapping("/admin/productos/{id}/editar")
    public String actualizarProducto(@PathVariable Long id,
                                     @ModelAttribute Producto producto,
                                     @RequestParam("imagenArchivo") MultipartFile imagenArchivo, Model model) throws IOException {
        try {
            productoService.actualizarProducto(id, producto, imagenArchivo);
            return "redirect:/admin/productos";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "FramesAdmin/frmEditarProducto";
        }
    }

}
