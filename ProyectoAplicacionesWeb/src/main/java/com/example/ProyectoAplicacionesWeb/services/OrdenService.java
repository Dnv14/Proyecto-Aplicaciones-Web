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

    public List<Orden> obtenerTodasLasOrdenes() { return ordenRepository.findAll(); }

    public Orden crearOrden(Orden orden) throws IOException {
        if(orden == null){
            throw new IllegalArgumentException("La orden no existe.");
        }
        if(orden.getIdOrden() == null){
            throw new IOException("El ID de la orden es obligatorio.");
        }
        if(orden.getFecha() == null){
            throw new IllegalArgumentException("La fecha de la orden es obligatoria.");
        }
        if(orden.getFecha().isAfter(LocalDate.now())){
            throw new IllegalArgumentException("La fecha de la orden no puede ser mayor a la fecha actual.");
        }
        if(orden.getTotal() == null){
            throw new IllegalArgumentException("El total de la orden es obligatorio.");
        }
        if(orden.getTotal().compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("El total de la orden no puede ser un negativo.");
        }
        if(orden.getCliente() == null){
            throw new IllegalArgumentException("La orden debe tener un cliente asociado.");
        }
        if(orden.getDireccion() == null){
            throw new IllegalArgumentException("La orden debe tener un direccion asociada.");
        }
        return ordenRepository.save(orden);
    }

    public Optional<Orden> buscarOrdenPorId(Long id) { return ordenRepository.findById(id); }

}
