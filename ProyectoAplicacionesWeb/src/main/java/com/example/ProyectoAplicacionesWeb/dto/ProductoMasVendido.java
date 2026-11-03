package com.example.ProyectoAplicacionesWeb.dto;

public record ProductoMasVendido(
        Long idProducto,
        String nombre,
        Long unidadesVendidas
) {
    
}
