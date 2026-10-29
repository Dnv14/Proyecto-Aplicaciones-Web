package com.example.ProyectoAplicacionesWeb.controllers;

import com.example.ProyectoAplicacionesWeb.model.enums.TipoMovimiento;
import com.example.ProyectoAplicacionesWeb.repository.InventarioRepository;
import com.example.ProyectoAplicacionesWeb.repository.ProductoRepository;
import com.example.ProyectoAplicacionesWeb.services.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping("/admin/ajustarInventario")
    public String mostrarInventario(@RequestParam(name="idProducto",required = false) Long idpProducto ,Model model) {
        if(idpProducto!=null){
            productoRepository.findById(idpProducto).ifPresent(producto -> model.addAttribute("producto", producto));
        }
        return "FramesAdmin/frmAjustarInventario";
    }

    @PostMapping("/admin/ajustarInventario")
    public String guardarAjuste(
            @RequestParam Long idProducto,
            @RequestParam Integer cantidad,
            @RequestParam TipoMovimiento tipoMovimiento,
            @RequestParam String comentario,
            RedirectAttributes redirectAttributes) {
        try {
            inventarioService.ajustarInventario(idProducto, cantidad, tipoMovimiento, comentario);
            redirectAttributes.addFlashAttribute("mensaje", "Inventario actualizado correctamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/ajustarInventario?idProducto="+idProducto;
    }


}
