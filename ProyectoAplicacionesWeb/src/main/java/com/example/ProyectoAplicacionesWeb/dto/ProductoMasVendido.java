package com.example.ProyectoAplicacionesWeb.dto;

//recibimos el ID, el nombre y la suma de unidades vendidas de un producto
public record ProductoMasVendido(
        Long idProducto,
        String nombre,
        Long unidadesVendidas
) {
    
}
