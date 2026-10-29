package com.example.ProyectoAplicacionesWeb.model;

import com.example.ProyectoAplicacionesWeb.model.enums.EstadoOrden;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Orden")
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrden;

    @Column
    @Enumerated(EnumType.STRING)
    private EstadoOrden estado;

    @Column
    private LocalDate fecha;

    @Column
    private BigDecimal total;

    @Column(length = 15, unique = true)
    private String folioOrden;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_direccion")
    private Direccion direccion;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<DetalleOrden> detalles;
}
