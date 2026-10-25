package com.example.ProyectoAplicacionesWeb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Carrito")
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCarrito;

    @Column
    private LocalDateTime fechaCreacion;

    @Column
    private LocalDateTime fechaActualizacion;

    @OneToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<DetalleCarrito> detalles;
}
