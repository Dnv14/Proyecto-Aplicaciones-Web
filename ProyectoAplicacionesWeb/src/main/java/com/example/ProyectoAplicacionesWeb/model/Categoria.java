package com.example.ProyectoAplicacionesWeb.model;

import lombok.Data;
import jakarta.persistence.*;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "Categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCategoria;

    @Column(length = 50)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Producto> productos;
}
