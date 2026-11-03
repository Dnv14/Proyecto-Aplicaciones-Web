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
    public String mostrarDashboard(@RequestParam(name = "mes", required = false) String mes, Model model) {
        YearMonth periodo = YearMonth.now();
        if (mes != null && !mes.isBlank()) {
            try {
                periodo = YearMonth.parse(mes); 
            } catch (DateTimeParseException ignored) {
                
            }
        }
        model.addAttribute("resumen", dashboardService.obtenerResumen(periodo));
        return "FramesAdmin/frmDashboard";
    }
}
