package com.example.ProyectoAplicacionesWeb.controllers;

import com.example.ProyectoAplicacionesWeb.model.Orden;
import com.example.ProyectoAplicacionesWeb.model.enums.EstadoOrden;
import com.example.ProyectoAplicacionesWeb.repository.CategoriaRepository;
import com.example.ProyectoAplicacionesWeb.repository.OrdenRepository;
import com.example.ProyectoAplicacionesWeb.services.OrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Optional;

@Controller
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @GetMapping("admin/ordenes")
    public String listarYMostrarOrdenes(@RequestParam(value = "id", required = false) Long idOrden, Model model) {
        model.addAttribute("ordenes", ordenService.obtenerTodasLasOrdenes());
        //Esto es cuando hace click en ver los detalles de la orden
        if (idOrden != null) {
            Optional<Orden> ordenSeleccionada = ordenService.buscarOrdenPorId(idOrden);
            ordenSeleccionada.ifPresent(orden -> model.addAttribute("ordenSeleccionada", orden));
        }
        return "FramesAdmin/frmListadoOrdenes";
    }

    @PostMapping("/admin/ordenes/actualizar-estado")
    public String actualizarEstadoOrden(
            @RequestParam("idOrden") Long idOrden,
            @RequestParam("nuevoEstado") EstadoOrden nuevoEstado) throws IOException {

        Optional<Orden> optionalOrden = ordenService.buscarOrdenPorId(idOrden);
        if (optionalOrden.isPresent()) {
            Orden orden = optionalOrden.get();
            orden.setEstado(nuevoEstado);
            ordenService.crearOrden(orden);
        }
        return "redirect:/admin/ordenes?id=" + idOrden;
    }

}
