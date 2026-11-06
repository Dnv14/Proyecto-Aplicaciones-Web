package com.example.ProyectoAplicacionesWeb.repository;

import com.example.ProyectoAplicacionesWeb.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//usamos JpaRepository para consultar y guardar los registros de producto por su ID
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    //buscamos productos por el nombre de su categoría
    List<Producto> findByCategoria_Nombre(String nombre);

    //buscamos coincidencias en el nombre sin distinguir mayúsculas de minúsculas
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    //contamos los productos que están activos
    long countByActivoTrue();

    //contamos los productos activos cuyo stock no supera el límite recibido
    long countByActivoTrueAndStockLessThanEqual(Integer stock);

    //obtenemos hasta cinco productos activos ordenados por nombre
    List<Producto> findTop5ByActivoTrueOrderByNombreAsc();

    //obtenemos hasta cinco productos con stock bajo, empezando por los que tienen menos unidades
    List<Producto> findTop5ByActivoTrueAndStockLessThanEqualOrderByStockAscNombreAsc(Integer stock);

}
