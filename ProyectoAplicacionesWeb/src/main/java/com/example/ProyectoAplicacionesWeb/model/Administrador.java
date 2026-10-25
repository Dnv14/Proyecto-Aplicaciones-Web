package com.example.ProyectoAplicacionesWeb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Administradores")
@PrimaryKeyJoinColumn(name = "id_usuario")
public class Administrador extends Usuario {
}