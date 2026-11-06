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

    //estas rutas muestran los formularios y el listado de categorías

    @GetMapping("/admin/categorias")

    //recibimos la búsqueda de la URL para filtrar las categorías por nombre
    public String mostrarCategorias(@RequestParam(name = "buscar", required = false) String buscar, Model model) {
        //si escribió algo buscamos por nombre, si viene vacío mostramos todas las categorías
        if (buscar != null && !buscar.isBlank()) {
            model.addAttribute("categorias", categoriaService.buscarPorNombre(buscar));
        } else {
            //cargamos las categorías que se van a mostrar en el listado
            model.addAttribute("categorias", categoriaService.findAll());
        }
        return "FramesAdmin/frmGestionarCategorias";
    }

    @GetMapping("/admin/crearCategoria")
    //enviamos una categoría vacía al formulario donde se va a registrar
    public String mostrarCrearCategoria(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "FramesAdmin/frmAgregarCategoria";
    }

    @GetMapping("/admin/editarCategoria/{id}")
    //obtenemos el ID de la URL y cargamos la categoría que se va a editar
    public String mostrarEditarCategoria(@PathVariable("id") Long id, Model model) {
        model.addAttribute("categoria", categoriaService.findById(id));
        return "FramesAdmin/frmEditarCategoria";
    }

    //estas rutas reciben los datos para crear, editar o eliminar categorías

    @PostMapping("/admin/crearCategoria")
    //recibimos la categoría del formulario y la mandamos al servicio para validarla y guardarla
    public String procesarCrearCategoria(@ModelAttribute Categoria categoria, Model model) {
        try {
            categoriaService.crearCategoria(categoria);
            return "redirect:/admin/categorias";

        } catch (IllegalArgumentException e) {
            //si ocurre un error enviamos el mensaje a la vista para mostrarlo
            model.addAttribute("error", e.getMessage());
            return "FramesAdmin/frmAgregarCategoria";
        }
    }

    @PostMapping("/admin/editarCategoria")
    //actualizamos los datos recibidos de la categoría y regresamos al listado
    public String procesarEditarCategoria(@ModelAttribute Categoria categoria, Model model) {
        try {
            categoriaService.actualizarCategoriaParcial(categoria.getIdCategoria(), categoria);
            return "redirect:/admin/categorias";

        } catch (IllegalArgumentException e) {
            //si ocurre un error enviamos el mensaje a la vista para mostrarlo
            model.addAttribute("error", e.getMessage());
            return "FramesAdmin/frmEditarCategoria";
        }
    }


    @PostMapping("/admin/eliminarCategoria/{id}")
    //intentamos eliminar la categoría usando el ID que viene en la URL
    public String eliminarCategoria(@PathVariable("id") Long id, Model model) {
        try {
            categoriaService.eliminarCategoria(id);
            return "redirect:/admin/categorias";
        } catch (RuntimeException e) {
            //si ocurre un error enviamos el mensaje a la vista para mostrarlo
            model.addAttribute("error", e.getMessage());
            //cargamos las categorías que se van a mostrar en el listado
            model.addAttribute("categorias", categoriaService.findAll());
            return "FramesAdmin/frmGestionarCategorias";
        }
    }
}
