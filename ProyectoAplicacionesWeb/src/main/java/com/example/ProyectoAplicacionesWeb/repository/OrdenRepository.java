package com.example.ProyectoAplicacionesWeb.repository;

import com.example.ProyectoAplicacionesWeb.model.Orden;
import com.example.ProyectoAplicacionesWeb.model.enums.EstadoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findAll();

    Optional<Orden> findById(Long id);

    long countByFecha(LocalDate fecha);

    long countByEstado(EstadoOrden estado);

    @Query("select coalesce(sum(o.total), 0) from Orden o where o.fecha >= :inicio and o.fecha < :fin")
    BigDecimal sumarTotalOrdenesEntreFechas(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    List<Orden> findTop5ByOrderByFechaDescIdOrdenDesc();
}
