package com.example.ProyectoAplicacionesWeb.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Clientes")
@PrimaryKeyJoinColumn(name = "id_usuario")

public class Cliente extends Usuario {
    @Column(length = 12)
    private String telefono;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Direccion> direcciones;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Orden> ordenes;

    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Carrito carrito;

}