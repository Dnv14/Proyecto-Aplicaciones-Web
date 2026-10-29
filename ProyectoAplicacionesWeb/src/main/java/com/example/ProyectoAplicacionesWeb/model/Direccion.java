package com.example.ProyectoAplicacionesWeb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Direccion")
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDireccion;

    @Column(length = 50)
    private String calle;

    @Column(length = 10)
    private String numero;

    @Column(length = 50)
    private String colonia;

    @Column(length = 50)
    private String ciudad;

    @Column(length = 5)
    private String codigoPostal;

    @Column(length = 50)
    private String estado;

    @Column(length = 100)
    private String referencias;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Cliente cliente;

    @OneToMany(mappedBy = "direccion", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Orden> ordenes;
}
