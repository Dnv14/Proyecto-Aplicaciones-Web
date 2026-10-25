package com.example.ProyectoAplicacionesWeb.model;

import com.example.ProyectoAplicacionesWeb.model.enums.TipoMovimiento;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "Inventario")
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idInventario;

    @Column
    private Integer cantidad;

    @Column
    private LocalDate fecha;

    @Column(length = 255)
    private String comentario;

    @Column
    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipoMovimiento;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;
}