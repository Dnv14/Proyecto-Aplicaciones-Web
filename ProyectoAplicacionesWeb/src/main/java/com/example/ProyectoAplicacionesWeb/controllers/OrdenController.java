package com.example.ProyectoAplicacionesWeb.controllers;

import com.example.ProyectoAplicacionesWeb.model.Orden;
import com.example.ProyectoAplicacionesWeb.model.enums.EstadoOrden;
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
    //mostramos todas las órdenes y, si recibimos un ID, también cargamos su detalle
    public String listarYMostrarOrdenes(@RequestParam(value = "id", required = false) Long idOrden, Model model) {
        model.addAttribute("ordenes", ordenService.obtenerTodasLasOrdenes());
        //si seleccionó ver el detalle recibimos el ID de esa orden
        if (idOrden != null) {
            Optional<Orden> ordenSeleccionada = ordenService.buscarOrdenPorId(idOrden);
            //enviamos el detalle a la vista solamente si encontramos la orden
            ordenSeleccionada.ifPresent(orden -> model.addAttribute("ordenSeleccionada", orden));
        }
        return "FramesAdmin/frmListadoOrdenes";
    }

    @PostMapping("/admin/ordenes/actualizar-estado")
    //recibimos el ID de la orden y el nuevo estado seleccionado en el formulario
    public String actualizarEstadoOrden(
            @RequestParam("idOrden") Long idOrden,
            @RequestParam("nuevoEstado") EstadoOrden nuevoEstado, Model model) throws IOException {


        try {
            Optional<Orden> optionalOrden = ordenService.buscarOrdenPorId(idOrden);
            //actualizamos el estado solamente si la orden existe
            if (optionalOrden.isPresent()) {
                Orden orden = optionalOrden.get();
                orden.setEstado(nuevoEstado);
                //guardamos la orden con su mismo ID para conservar el cambio de estado
                ordenService.crearOrden(orden);
            }
            //regresamos al listado manteniendo abierta la orden seleccionada
            return "redirect:/admin/ordenes?id=" + idOrden;
        } catch (IllegalArgumentException e) {
            //enviamos el mensaje de validación a la vista
            model.addAttribute("error", e.getMessage());
            return "FramesAdmin/frmListadoOrdenes";
        }
    }

}
