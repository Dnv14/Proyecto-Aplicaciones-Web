package com.example.ProyectoAplicacionesWeb.services;

import com.example.ProyectoAplicacionesWeb.model.Inventario;
import com.example.ProyectoAplicacionesWeb.model.Producto;
import com.example.ProyectoAplicacionesWeb.model.enums.TipoMovimiento;
import com.example.ProyectoAplicacionesWeb.repository.InventarioRepository;
import com.example.ProyectoAplicacionesWeb.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.hibernate.validator.internal.constraintvalidators.hv.Mod10CheckValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class InventarioService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    @Transactional
    public void ajustarInventario(Long idProducto, Integer cantidad, TipoMovimiento tipoMovimiento, String comentario) {
        if (cantidad == null || cantidad < 0 || (cantidad == 0 && tipoMovimiento != TipoMovimiento.CORRECCION)) {
            throw new IllegalArgumentException("Cantidad no válida");
        }
        if(comentario == null || comentario.isBlank()) {
            throw new IllegalArgumentException("El motivo del ajuste es obligatorio");
        }
        Producto producto = productoRepository.findById(idProducto).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if (tipoMovimiento == null) {
            throw new IllegalArgumentException("Debes seleccionar un tipo de movimiento");
        }
        int stockActual = producto.getStock();
        int nuevoStock = stockActual;
        switch (tipoMovimiento) {
            case ENTRADA:
                nuevoStock += cantidad;
                break;

            case SALIDA:
            case MERMA:
                nuevoStock -= cantidad;
                break;
            case CORRECCION:
                nuevoStock = cantidad;
                break;
        }
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("No hay suficiente stock para realizar el movimiento");
        }
        producto.setStock(nuevoStock);
        productoRepository.save(producto);
        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setCantidad(cantidad);
        inventario.setFecha(LocalDate.now());
        inventario.setComentario(comentario);
        inventario.setTipoMovimiento(tipoMovimiento);
        inventarioRepository.save(inventario);
    }
}