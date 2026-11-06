package com.example.ProyectoAplicacionesWeb.controllers;

import com.example.ProyectoAplicacionesWeb.model.enums.TipoMovimiento;
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
    //recibimos un ID opcional para cargar el producto que se quiere ajustar
    public String mostrarInventario(@RequestParam(name = "idProducto", required = false) Long idpProducto, Model model) {
        //si llegó un ID buscamos el producto y lo enviamos al HTML solamente si existe
        if (idpProducto != null) {
            productoRepository.findById(idpProducto).ifPresent(producto -> model.addAttribute("producto", producto));
        }
        return "FramesAdmin/frmAjustarInventario";
    }

    @PostMapping("/admin/ajustarInventario")
    //recibimos el producto, la cantidad, el tipo de movimiento y el motivo del ajuste
    public String guardarAjuste(
            @RequestParam Long idProducto,
            @RequestParam Integer cantidad,
            @RequestParam TipoMovimiento tipoMovimiento,
            @RequestParam String comentario,
            RedirectAttributes redirectAttributes) {
        try {
            //el servicio valida el movimiento, actualiza el stock y guarda el historial
            inventarioService.ajustarInventario(idProducto, cantidad, tipoMovimiento, comentario);
            //guardamos un mensaje que se mostrará después de la redirección
            redirectAttributes.addFlashAttribute("mensaje", "Inventario actualizado correctamente");
        } catch (IllegalArgumentException e) {
            //si el ajuste no es válido mostramos el motivo al regresar al formulario
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        //volvemos al mismo producto para consultar cómo quedó su inventario
        return "redirect:/admin/ajustarInventario?idProducto=" + idProducto;
    }

}
