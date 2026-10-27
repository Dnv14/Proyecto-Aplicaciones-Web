package com.example.ProyectoAplicacionesWeb.controllers;

import com.example.ProyectoAplicacionesWeb.model.Usuario;
import com.example.ProyectoAplicacionesWeb.services.AdministradorService;
import com.example.ProyectoAplicacionesWeb.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
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

    @PostMapping("/login")
    public String procesarLogin(@RequestParam("correo") String correo,
                                @RequestParam("contrasenia") String contrasenia, HttpSession session, Model model) {

        Usuario usuario = usuarioService.autenticarUsuario(correo, contrasenia);

        if (usuario != null) {
            session.setAttribute("usuario_id", usuario.getIdUsuario());
            return "redirect:/frmPrincipalAdministrador";
        } else {
            model.addAttribute("error",
                    "Credenciales incorrectas");
            return "frmLogin";
        }
    }

    //Registro
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "frmRegistro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(Usuario usuario) {
        usuarioService.registrarUsuario(usuario);
        return "redirect:/login?registrado=true";
    }

    //logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
