package com.example.ProyectoAplicacionesWeb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class InicioController {

    @GetMapping("/admin")
    //mandamos al administrador al panel de resumen
    public String inicioAdministrador() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/cliente")
    //mostramos la página principal del cliente
    public String inicioCliente() {
        return "FramesUsuario/frmPrincipalUsuario";
    }
}
