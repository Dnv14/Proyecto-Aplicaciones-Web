package com.example.ProyectoAplicacionesWeb.controllers;

import com.example.ProyectoAplicacionesWeb.model.Cliente;
import com.example.ProyectoAplicacionesWeb.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    //Login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "frmLogin";
    }

    //Registro
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "frmRegistro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(Cliente cliente,
                                   @RequestParam("confirmarCorreo") String correo,
                                   @RequestParam("confirmarContrasenia") String contrasenia){
        if(!cliente.getCorreo().equals(correo)) {
            return "redirect:/registro?errorCorreo";
        }
        if(!cliente.getContrasenia().equals(contrasenia)) {
            return "redirect:/registro?errorContrasenia";
        }
        if (usuarioService.existeCorreo(cliente.getCorreo())) {
            return "redirect:/registro?errorCorreoExistente";
        }
        usuarioService.registrarUsuario(cliente);
        return "redirect:/login?registrado=true";
    }
}
