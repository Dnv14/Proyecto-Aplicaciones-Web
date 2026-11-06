package com.example.ProyectoAplicacionesWeb.dto;

import java.math.BigDecimal;
import java.util.List;

import com.example.ProyectoAplicacionesWeb.model.Orden;
import com.example.ProyectoAplicacionesWeb.model.Producto;

//agrupamos los datos del panel en un solo objeto para enviarlos a la vista
public record DashboardResumen(long totalOrdenes,
        long ordenesHoy,
        long ordenesPendientes,
        long productosActivos,
        long productosStockBajo,
        BigDecimal valorOrdenesPeriodo,
        String nombrePeriodo,
        String mesSeleccionado,
        List<EstadoOrdenResumen> estados,
        List<Orden> ordenesRecientes,
        List<Producto> productosStockCritico,
        List<ProductoMasVendido> productosMasVendidos) {
        
}
