package com.example.ProyectoAplicacionesWeb.services;

import com.example.ProyectoAplicacionesWeb.model.Producto;
import com.example.ProyectoAplicacionesWeb.repository.CategoriaRepository;
import com.example.ProyectoAplicacionesWeb.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private static final String UPLOAD_DIR= "src/main/resources/static/uploads/productos/";

    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto guardarProducto(Producto producto, MultipartFile imagenArchivo) throws IOException {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (producto.getPrecio() == null ||
                producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio del producto debe ser mayor a 0");
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock del producto no puede ser negativo ni nulo");
        }
        if (producto.getActivo() == null) {
            throw new IllegalArgumentException("El estado del producto es obligatorio");
        }
        if (producto.getCategoria() == null) {
            throw new IllegalArgumentException("La categoría del producto es obligatoria");
        }
        if (producto.getCategoria().getIdCategoria() == null || !categoriaRepository.existsById(producto.getCategoria().getIdCategoria())) {
            throw new IllegalArgumentException("La categoría del producto debe existir");
        }
        if (producto.getNombre().length() > 50) {
            throw new IllegalArgumentException("El nombre no puede superar los 50 caracteres");
        }
        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
            String nombreArchivo = guardarImagenLocal(imagenArchivo);
            producto.setImagen("/uploads/productos/" + nombreArchivo);
        }
        return productoRepository.save(producto);
    }

    public String guardarImagenLocal(MultipartFile imagenArchivo) throws IOException {
        if(imagenArchivo==null || imagenArchivo.isEmpty()){
            return null;
        }
        String nombreOriginal=imagenArchivo.getOriginalFilename();
        if(nombreOriginal==null || nombreOriginal.isBlank()){
            throw new IllegalArgumentException("La imagen del producto debe tener un nombre valido");
        }
        int posicionPunto=nombreOriginal.lastIndexOf(".");
        if(posicionPunto==-1){
            throw new IllegalArgumentException("La imagen debe tener una extencion");
        }
        String extension=nombreOriginal.substring(posicionPunto).toLowerCase();
        String tipo=imagenArchivo.getContentType();
        boolean formatoValido =
                (extension.equals(".jpg") || extension.equals(".jpeg"))
                        && "image/jpeg".equals(tipo)
                        || extension.equals(".png") && "image/png".equals(tipo)
                        || extension.equals(".webp") && "image/webp".equals(tipo);

        if(!formatoValido){
            throw new IllegalArgumentException("Solo se permiten imagenes JPG, PNG o WEBP");
        }

        Path ruta= Paths.get(UPLOAD_DIR);
        if(!Files.exists(ruta)){
            Files.createDirectories(ruta);
        }

        String nombreArchivo= UUID.randomUUID()+extension;
        Files.copy(imagenArchivo.getInputStream(),ruta.resolve(nombreArchivo));

        return nombreArchivo;
    }

    public List<Producto> obtenerProductosPorCategoria(String categoria) {
        return productoRepository.findByCategoria_Nombre(categoria);
    }

    public Producto cambiarEstadoProducto(Long idProducto, boolean activo) {
        Producto producto= productoRepository.findById(idProducto).orElseThrow(()->new RuntimeException("Producto no encontrado"));
        producto.setActivo(activo);
        return productoRepository.save(producto);
    }

}
