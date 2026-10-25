package com.example.ProyectoAplicacionesWeb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DetalleCarrito")
public class DetalleCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalleCarrito;

    @Column
    private Integer cantidad;

    @Column
    private BigDecimal subtotalCalculado;

    @ManyToOne
    @JoinColumn(name = "id_carrito")
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;
}
