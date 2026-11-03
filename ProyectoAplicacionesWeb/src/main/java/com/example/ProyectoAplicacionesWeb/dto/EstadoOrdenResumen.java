package com.example.ProyectoAplicacionesWeb.dto;

import com.example.ProyectoAplicacionesWeb.model.enums.EstadoOrden;

public record EstadoOrdenResumen(
        EstadoOrden estado,
        String etiqueta,
        long cantidad,
        int porcentaje
) {
    
}
