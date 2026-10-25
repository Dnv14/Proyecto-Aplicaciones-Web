package com.example.ProyectoAplicacionesWeb.model;

import lombok.Data;
import jakarta.persistence.*;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "Productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;

    @Column(length = 50)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column
    private Double precio;

    @Column(length = 255)
    private String imagen;

    @Column
    private Boolean activo;

    @Column
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Inventario> historicoInventario;
}
