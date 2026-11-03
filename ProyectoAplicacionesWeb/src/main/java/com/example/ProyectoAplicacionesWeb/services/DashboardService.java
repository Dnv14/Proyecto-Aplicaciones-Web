package com.example.ProyectoAplicacionesWeb.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.ProyectoAplicacionesWeb.dto.DashboardResumen;
import com.example.ProyectoAplicacionesWeb.dto.EstadoOrdenResumen;
import com.example.ProyectoAplicacionesWeb.model.enums.EstadoOrden;
import com.example.ProyectoAplicacionesWeb.repository.DetalleOrdenRepository;
import com.example.ProyectoAplicacionesWeb.repository.OrdenRepository;
import com.example.ProyectoAplicacionesWeb.repository.ProductoRepository;

import jakarta.transaction.Transactional;

@Service 
public class DashboardService {
    
    private static final int STOCK_BAJO_LIMITE = 5;
    private static final DateTimeFormatter FORMATO_MES = DateTimeFormatter.ofPattern("MMMM yyyy");

    private final OrdenRepository ordenRepository;
    private final ProductoRepository productoRepository;
    private final DetalleOrdenRepository detalleOrdenRepository;

    public DashboardService(OrdenRepository ordenRepository, ProductoRepository productoRepository, DetalleOrdenRepository detalleOrdenRepository) {
        this.ordenRepository = ordenRepository;
        this.productoRepository = productoRepository;
        this.detalleOrdenRepository = detalleOrdenRepository;
    }

    @Transactional 
    public DashboardResumen obtenerResumen(YearMonth mes) {
        LocalDate mesInicio = mes.atDay(1);
        LocalDate mesFin = mes.plusMonths(1).atDay(1);

        long totalOrdenes = ordenRepository.count();
        long ordenesHoy = ordenRepository.countByFecha(LocalDate.now());
        long productosActivos = productoRepository.countByActivoTrue();
        long productosStockBajo = productoRepository.countByActivoTrueAndStockLessThanEqual(STOCK_BAJO_LIMITE);

        List<EstadoOrdenResumen> estados = new ArrayList<>();
        long ordenesPendientes = 0;
        for(EstadoOrden estado : EstadoOrden.values()) {
            long cantidad = ordenRepository.countByEstado(estado);
            if(estado == EstadoOrden.PENDIENTE) {
                ordenesPendientes = cantidad;
            }
            int porcentaje = totalOrdenes > 0 ? (int) ((cantidad * 100) / totalOrdenes) : 0;
            estados.add(new EstadoOrdenResumen(estado, etiquetaEstadoOrden(estado.name()), cantidad, porcentaje));
        }

        BigDecimal valorOrdenesPeriodo = ordenRepository.sumarTotalOrdenesEntreFechas(mesInicio, mesFin);
        if(valorOrdenesPeriodo == null) {
            valorOrdenesPeriodo = BigDecimal.ZERO;
        }

        return new DashboardResumen(
                totalOrdenes,
                ordenesHoy,
                ordenesPendientes,
                productosActivos,
                productosStockBajo,
                valorOrdenesPeriodo,
                mesInicio.format(FORMATO_MES),
                mesInicio.getMonth().name(),
                estados,
                ordenRepository.findTop5ByOrderByFechaDescIdOrdenDesc(),
                productoRepository.findTop5ByActivoTrueAndStockLessThanEqualOrderByStockAscNombreAsc(STOCK_BAJO_LIMITE),
                detalleOrdenRepository.productosMasVendidosDelPeriodo(mesInicio, mesFin, PageRequest.of(0, 5)));
    }

    private String etiquetaEstadoOrden(String estado) {
        return switch (estado) {
            case "PENDIENTE" -> "Pendiente";
            case "EN_PROCESO" -> "En Proceso";
            case "COMPLETADA" -> "Completada";
            case "CANCELADA" -> "Cancelada";
            default -> "Desconocido";
        };
    }

}
