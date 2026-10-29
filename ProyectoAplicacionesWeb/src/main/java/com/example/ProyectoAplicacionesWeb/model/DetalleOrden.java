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
@Table(name = "DetalleOrden")
public class DetalleOrden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleOrden;

    @Column
    private Integer cantidad;

    @Column
    private BigDecimal subtotal;

    @Column
    private BigDecimal precioUnitario;

    @ManyToOne
    @JoinColumn(name = "id_orden")
    private Orden orden;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;
}
