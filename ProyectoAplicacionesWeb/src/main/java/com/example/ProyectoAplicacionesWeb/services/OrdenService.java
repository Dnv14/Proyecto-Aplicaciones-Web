package com.example.ProyectoAplicacionesWeb.services;

import com.example.ProyectoAplicacionesWeb.model.Orden;
import com.example.ProyectoAplicacionesWeb.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    //obtenemos las órdenes que se van a mostrar en el listado
    public List<Orden> obtenerTodasLasOrdenes() { return ordenRepository.findAll(); }

    //validamos los datos de la orden antes de guardarla
    public Orden crearOrden(Orden orden) throws IOException {
        //revisamos que se haya recibido una orden
        if(orden == null){
            throw new IllegalArgumentException("La orden no existe.");
        }
        //este método exige que la orden ya tenga un ID
        if(orden.getIdOrden() == null){
            throw new IOException("El ID de la orden es obligatorio.");
        }
        //validamos que la orden tenga una fecha
        if(orden.getFecha() == null){
            throw new IllegalArgumentException("La fecha de la orden es obligatoria.");
        }
        //no permitimos una fecha posterior al día actual
        if(orden.getFecha().isAfter(LocalDate.now())){
            throw new IllegalArgumentException("La fecha de la orden no puede ser mayor a la fecha actual.");
        }
        //revisamos que se haya indicado el total
        if(orden.getTotal() == null){
            throw new IllegalArgumentException("El total de la orden es obligatorio.");
        }
        //comprobamos que el total no sea negativo
        if(orden.getTotal().compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("El total de la orden no puede ser un negativo.");
        }
        //validamos que la orden esté asociada a un cliente
        if(orden.getCliente() == null){
            throw new IllegalArgumentException("La orden debe tener un cliente asociado.");
        }
        //validamos que tenga una dirección asociada
        if(orden.getDireccion() == null){
            throw new IllegalArgumentException("La orden debe tener un direccion asociada.");
        }
        return ordenRepository.save(orden);
    }

    //buscamos la orden por ID, el resultado queda vacío si no existe
    public Optional<Orden> buscarOrdenPorId(Long id) { return ordenRepository.findById(id); }

}
