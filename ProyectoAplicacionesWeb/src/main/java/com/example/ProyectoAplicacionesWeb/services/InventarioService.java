package com.example.ProyectoAplicacionesWeb.services;

import com.example.ProyectoAplicacionesWeb.model.Inventario;
import com.example.ProyectoAplicacionesWeb.model.Producto;
import com.example.ProyectoAplicacionesWeb.model.enums.TipoMovimiento;
import com.example.ProyectoAplicacionesWeb.repository.InventarioRepository;
import com.example.ProyectoAplicacionesWeb.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class InventarioService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    //guardamos el cambio de stock y el movimiento en una misma transacción
    @Transactional
    //ajustamos las existencias según el tipo de movimiento y dejamos registrado el motivo
    public void ajustarInventario(Long idProducto, Integer cantidad, TipoMovimiento tipoMovimiento, String comentario) {
        //rechazamos cantidades negativas o vacías, el cero solo se permite en una corrección
        if (cantidad == null || cantidad < 0 || (cantidad == 0 && tipoMovimiento != TipoMovimiento.CORRECCION)) {
            throw new IllegalArgumentException("Cantidad no válida");
        }
        //pedimos un motivo para saber por qué se hizo el ajuste
        if (comentario == null || comentario.isBlank()) {
            throw new IllegalArgumentException("El motivo del ajuste es obligatorio");
        }
        //buscamos el producto y detenemos el ajuste si no existe
        Producto producto = productoRepository.findById(idProducto).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        //validamos que se haya seleccionado el tipo de movimiento
        if (tipoMovimiento == null) {
            throw new IllegalArgumentException("Debes seleccionar un tipo de movimiento");
        }
        //tomamos las existencias actuales como punto de partida
        int stockActual = producto.getStock();
        int nuevoStock = stockActual;
        switch (tipoMovimiento) {
            //en una entrada sumamos la cantidad recibida
            case ENTRADA:
                nuevoStock += cantidad;
                break;

            //en una salida o merma descontamos la cantidad
            case SALIDA:
            case MERMA:
                nuevoStock -= cantidad;
                break;
            //en una corrección la cantidad recibida es el nuevo stock total
            case CORRECCION:
                nuevoStock = cantidad;
                break;
        }
        //evitamos que una salida o merma deje las existencias en negativo
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("No hay suficiente stock para realizar el movimiento");
        }
        //guardamos las nuevas existencias del producto
        producto.setStock(nuevoStock);
        productoRepository.save(producto);
        //registramos el producto, la cantidad, la fecha y el motivo para conservar el historial
        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setCantidad(cantidad);
        inventario.setFecha(LocalDate.now());
        inventario.setComentario(comentario);
        inventario.setTipoMovimiento(tipoMovimiento);
        inventarioRepository.save(inventario);
    }
}