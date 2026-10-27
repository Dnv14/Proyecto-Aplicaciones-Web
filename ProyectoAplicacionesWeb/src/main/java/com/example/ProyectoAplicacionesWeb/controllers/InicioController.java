package com.example.ProyectoAplicacionesWeb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class InicioController {

    @GetMapping("/admin")
    public String inicioAdministrador() {
        return "FramesAdmin/frmPrincipalAdministrador";
    }

    @GetMapping("/cliente")
    public String inicioCliente() {
        return "FramesUsuario/frmPrincipalUsuario";
    }
}
