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
    //mostramos el formulario de productos y cargamos las categorías que se pueden seleccionar
    public String mostrarCrearProducto(Model model) {
        //enviamos las categorías al HTML para llenar la lista del formulario
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "FramesAdmin/frmCrearProducto";
    }

    @GetMapping("/admin/productos")
    //mostramos el inventario por nombre o ID, recibimos la búsqueda de la URL y el modelo para enviar los datos
    public String mostrarProductos(@RequestParam(name = "buscar", required = false) String buscar, Model model) {
        //validamos si no se buscó algo o si el texto viene vacío
        if (buscar == null || buscar.isBlank()) {
            //mostramos todos los productos registrados porque no se indicó una búsqueda
            model.addAttribute("productos", productoService.obtenerProductos());
            //si hay una búsqueda revisamos si contiene únicamente dígitos
        } else if (buscar.matches("\\d+")) {
            //convertimos el texto a Long y buscamos el producto por su ID
            model.addAttribute("productos", productoService.buscarProductoPorId(Long.valueOf(buscar)));
        } else {
            //si el texto no está vacío ni contiene solo dígitos buscamos por nombre
            model.addAttribute("productos", productoService.buscarProductosPorNombre(buscar));
        }
        //enviamos lo buscado al HTML para conservarlo en el campo de búsqueda
        model.addAttribute("busquedaActual", buscar);
        //indicamos la vista que se debe mostrar
        return "FramesAdmin/frmInventario";
    }


    @PostMapping("/admin/crearProducto")
    //recibimos los datos del formulario y la imagen que seleccionó el usuario
    public String crearProducto(@ModelAttribute Producto producto,
                                @RequestParam("imagenArchivo") MultipartFile imagenArchivo, Model model) throws IOException {
        try {
            //validamos y guardamos el producto junto con su imagen
            productoService.guardarProducto(producto, imagenArchivo);
            return "redirect:/admin/productos";
        } catch (IllegalArgumentException e) {
            //enviamos el mensaje de la validación al formulario para que se pueda corregir
            model.addAttribute("error", e.getMessage());
            return "FramesAdmin/frmCrearProducto";
        }
    }

    @PostMapping("/admin/productos/{id}/estado")
    //recibimos el ID de la URL para activar o desactivar el producto
    public String cambiarEstadoProducto(@PathVariable Long id) {
        Producto producto = productoService.obtenerProductoPorId(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        //invertimos el estado actual, si estaba activo lo desactivamos y al revés
        productoService.cambiarEstadoProducto(id, !producto.getActivo());
        return "redirect:/admin/productos";
    }

    @GetMapping("/admin/productos/{id}/editar")
    //buscamos el producto por su ID para mostrar sus datos en el formulario de edición
    public String mostrarEditarProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.obtenerProductoPorId(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        //enviamos el producto encontrado para llenar los campos
        model.addAttribute("producto", producto);
        //enviamos las categorías al HTML para llenar la lista del formulario
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "FramesAdmin/frmEditarProducto";
    }

    @PostMapping("/admin/productos/{id}/editar")
    //recibimos el ID, los datos editados y la imagen para actualizar el producto
    public String actualizarProducto(@PathVariable Long id,
                                     @ModelAttribute Producto producto,
                                     @RequestParam("imagenArchivo") MultipartFile imagenArchivo, Model model) throws IOException {
        try {
            //mandamos los cambios al servicio, donde se validan antes de guardarlos
            productoService.actualizarProducto(id, producto, imagenArchivo);
            return "redirect:/admin/productos";
        } catch (IllegalArgumentException e) {
            //enviamos el mensaje de la validación al formulario para que se pueda corregir
            model.addAttribute("error", e.getMessage());
            return "FramesAdmin/frmEditarProducto";
        }
    }

}
