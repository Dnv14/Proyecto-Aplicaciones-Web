package com.example.ProyectoAplicacionesWeb.controllers;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ProyectoAplicacionesWeb.services.DashboardService;

@Controller
public class DashboardController {
     private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/admin/dashboard")
    //recibimos el mes de la URL para mostrar el resumen del panel
    public String mostrarDashboard(@RequestParam(name = "mes", required = false) String mes, Model model) {
        //usamos el mes actual cuando no se selecciona otro periodo
        YearMonth periodo = YearMonth.now();
        if (mes != null && !mes.isBlank()) {
            try {
                //convertimos el texto recibido a un año y mes
                periodo = YearMonth.parse(mes); 
            //si el formato no es válido conservamos el mes actual
            } catch (DateTimeParseException ignored) {
                
            }
        }
        //obtenemos los conteos y las listas del panel y los enviamos al HTML
        model.addAttribute("resumen", dashboardService.obtenerResumen(periodo));
        return "FramesAdmin/frmDashboard";
    }
}
