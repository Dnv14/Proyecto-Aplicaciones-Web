package com.example.ProyectoAplicacionesWeb.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ProyectoAplicacionesWeb.dto.ProductoMasVendido;
import com.example.ProyectoAplicacionesWeb.model.DetalleOrden;

//usamos JpaRepository para consultar y guardar los registros de detalles de órdenes por su ID
public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Long> {
    //sumamos las unidades por producto dentro del periodo y ordenamos de mayor a menor venta
    @Query("""
            select new com.example.ProyectoAplicacionesWeb.dto.ProductoMasVendido(
                p.idProducto, p.nombre, sum(d.cantidad))
            from DetalleOrden d
            join d.producto p
            join d.orden o
            where o.fecha >= :inicio and o.fecha < :fin
            group by p.idProducto, p.nombre
            order by sum(d.cantidad) desc, p.nombre asc
            """)
    //recibimos las fechas y la paginación para limitar cuántos productos devuelve la consulta
    List<ProductoMasVendido> productosMasVendidosDelPeriodo(
            @Param("inicio") LocalDate inicio,
            @Param("fin") LocalDate fin,
            Pageable limite
    );
}
