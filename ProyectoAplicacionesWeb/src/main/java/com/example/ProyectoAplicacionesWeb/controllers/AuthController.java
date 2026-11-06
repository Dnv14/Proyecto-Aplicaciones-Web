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

    //mostramos el formulario de inicio de sesión
    @GetMapping("/login")
    public String mostrarLogin() {
        return "frmLogin";
    }

    //mostramos el formulario de registro
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        //enviamos un cliente vacío para capturar sus datos en el registro
        model.addAttribute("cliente", new Cliente());
        return "frmRegistro";
    }

    @PostMapping("/registro")
    //recibimos los datos del cliente y los campos de confirmación del formulario
    public String procesarRegistro(Cliente cliente,
                                   @RequestParam("confirmarCorreo") String correo,
                                   @RequestParam("confirmarContrasenia") String contrasenia){
        //validamos que el correo y su confirmación sean iguales
        if(!cliente.getCorreo().equals(correo)) {
            return "redirect:/registro?errorCorreo";
        }
        //validamos que las dos contraseñas coincidan
        if(!cliente.getContrasenia().equals(contrasenia)) {
            return "redirect:/registro?errorContrasenia";
        }
        //revisamos que el correo no esté registrado por otro usuario
        if (usuarioService.existeCorreo(cliente.getCorreo())) {
            return "redirect:/registro?errorCorreoExistente";
        }
        //registramos al cliente y después lo mandamos al login con el aviso de registro
        usuarioService.registrarUsuario(cliente);
        return "redirect:/login?registrado=true";
    }
}
