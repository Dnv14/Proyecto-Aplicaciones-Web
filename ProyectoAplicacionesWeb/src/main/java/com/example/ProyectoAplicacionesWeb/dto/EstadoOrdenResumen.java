package com.example.ProyectoAplicacionesWeb.dto;

import com.example.ProyectoAplicacionesWeb.model.enums.EstadoOrden;

//reunimos el estado de la orden, su etiqueta, la cantidad y su porcentaje
public record EstadoOrdenResumen(
        EstadoOrden estado,
        String etiqueta,
        long cantidad,
        int porcentaje
) {
    
}
