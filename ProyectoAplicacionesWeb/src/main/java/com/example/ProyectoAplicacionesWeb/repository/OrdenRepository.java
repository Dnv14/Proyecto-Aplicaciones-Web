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
//usamos JpaRepository para consultar y guardar los registros de orden por su ID
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findAll();

    Optional<Orden> findById(Long id);

    //contamos las órdenes registradas en la fecha indicada
    long countByFecha(LocalDate fecha);

    //contamos las órdenes que tienen el estado recibido
    long countByEstado(EstadoOrden estado);

    //sumamos los totales incluyendo la fecha inicial y excluyendo la final, si no hay órdenes devolvemos cero
    @Query("select coalesce(sum(o.total), 0) from Orden o where o.fecha >= :inicio and o.fecha < :fin")
    BigDecimal sumarTotalOrdenesEntreFechas(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    //obtenemos las cinco órdenes más recientes, si coinciden en fecha ordenamos por ID
    List<Orden> findTop5ByOrderByFechaDescIdOrdenDesc();
}
