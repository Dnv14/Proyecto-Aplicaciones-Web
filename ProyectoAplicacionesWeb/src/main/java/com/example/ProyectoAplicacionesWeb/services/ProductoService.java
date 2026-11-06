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
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private static final String UPLOAD_DIR= "src/main/resources/static/uploads/productos/";

    //obtenemos todos los productos registrados
    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

    //buscamos por ID, el resultado puede venir vacío si el producto no existe
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    //validamos los datos del producto antes de guardar el registro
    public Producto guardarProducto(Producto producto, MultipartFile imagenArchivo) throws IOException {
        //revisamos que el nombre tenga contenido
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        //validamos que el precio exista y sea mayor a cero
        if (producto.getPrecio() == null ||
                producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio del producto debe ser mayor a 0");
        }
        //el stock debe tener un valor y no puede ser negativo
        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock del producto no puede ser negativo ni nulo");
        }
        //revisamos que se indique si el producto está activo
        if (producto.getActivo() == null) {
            throw new IllegalArgumentException("El estado del producto es obligatorio");
        }
        //validamos que el producto tenga una categoría
        if (producto.getCategoria() == null) {
            throw new IllegalArgumentException("La categoría del producto es obligatoria");
        }
        //comprobamos que el ID de la categoría exista en la base de datos
        if (producto.getCategoria().getIdCategoria() == null || !categoriaRepository.existsById(producto.getCategoria().getIdCategoria())) {
            throw new IllegalArgumentException("La categoría del producto debe existir");
        }
        //limitamos el nombre a los 50 caracteres que permite la columna
        if (producto.getNombre().length() > 50) {
            throw new IllegalArgumentException("El nombre no puede superar los 50 caracteres");
        }
        //si recibimos una imagen la guardamos y asignamos su ruta al producto
        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
            String nombreArchivo = guardarImagenLocal(imagenArchivo);
            producto.setImagen("/uploads/productos/" + nombreArchivo);
        }
        return productoRepository.save(producto);
    }

    //guardamos la imagen en la carpeta de productos y devolvemos el nombre del archivo
    public String guardarImagenLocal(MultipartFile imagenArchivo) throws IOException {
        //si no se recibió un archivo no hay imagen que guardar
        if(imagenArchivo==null || imagenArchivo.isEmpty()){
            return null;
        }
        //obtenemos el nombre original para revisar la extensión del archivo
        String nombreOriginal=imagenArchivo.getOriginalFilename();
        if(nombreOriginal==null || nombreOriginal.isBlank()){
            throw new IllegalArgumentException("La imagen del producto debe tener un nombre valido");
        }
        //buscamos el último punto para separar la extensión del nombre
        int posicionPunto=nombreOriginal.lastIndexOf(".");
        if(posicionPunto==-1){
            throw new IllegalArgumentException("La imagen debe tener una extencion");
        }
        String extension=nombreOriginal.substring(posicionPunto).toLowerCase();
        String tipo=imagenArchivo.getContentType();
        //comprobamos que la extensión y el tipo declarado coincidan con JPG, PNG o WEBP
        boolean formatoValido =
                (extension.equals(".jpg") || extension.equals(".jpeg"))
                        && "image/jpeg".equals(tipo)
                        || extension.equals(".png") && "image/png".equals(tipo)
                        || extension.equals(".webp") && "image/webp".equals(tipo);

        if(!formatoValido){
            throw new IllegalArgumentException("Solo se permiten imagenes JPG, PNG o WEBP");
        }

        //preparamos la carpeta de imágenes y la creamos si todavía no existe
        Path ruta= Paths.get(UPLOAD_DIR);
        if(!Files.exists(ruta)){
            Files.createDirectories(ruta);
        }

        //generamos un nombre aleatorio para evitar repetir los nombres de las imágenes
        String nombreArchivo= UUID.randomUUID()+extension;
        //copiamos el contenido recibido al archivo dentro de la carpeta de productos
        Files.copy(imagenArchivo.getInputStream(),ruta.resolve(nombreArchivo));

        return nombreArchivo;
    }

    //obtenemos los productos de la categoría indicada por su nombre
    public List<Producto> obtenerProductosPorCategoria(String categoria) {
        return productoRepository.findByCategoria_Nombre(categoria);
    }

    //buscamos el producto y guardamos el estado activo o inactivo que recibimos
    public Producto cambiarEstadoProducto(Long idProducto, boolean activo) {
        Producto producto= productoRepository.findById(idProducto).orElseThrow(()->new RuntimeException("Producto no encontrado"));
        producto.setActivo(activo);
        return productoRepository.save(producto);
    }

    //validamos los datos editados antes de modificar el producto existente
    public Producto actualizarProducto(Long idProducto,Producto datosActualizados, MultipartFile imagenArchivo) throws IOException {
        //revisamos que el nombre no venga vacío
        if (datosActualizados.getNombre() == null ||
                datosActualizados.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }

        //validamos que el nombre no supere el tamaño permitido
        if (datosActualizados.getNombre().length() > 50) {
            throw new IllegalArgumentException("El nombre no puede superar los 50 caracteres");
        }

        //revisamos que el precio sea mayor a cero
        if (datosActualizados.getPrecio() == null ||
                datosActualizados.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }

        //validamos que se haya seleccionado una categoría con ID
        if(datosActualizados.getCategoria()==null || datosActualizados.getCategoria().getIdCategoria()==null){
            throw new IllegalArgumentException("La categoria es obligatoria");
        }

        //comprobamos que la categoría seleccionada siga registrada
        if(!categoriaRepository.existsById(datosActualizados.getCategoria().getIdCategoria())){
            throw new IllegalArgumentException("La categoria seleccionada no existe");
        }

        Producto producto=productoRepository.findById(idProducto).orElseThrow(()->new RuntimeException("Producto no encontrado"));
        //copiamos los datos editables al producto que encontramos en la base de datos
        producto.setNombre(datosActualizados.getNombre());
        producto.setDescripcion(datosActualizados.getDescripcion());
        producto.setPrecio(datosActualizados.getPrecio());
        producto.setCategoria(datosActualizados.getCategoria());

        //reemplazamos la ruta de la imagen solo si se subió un archivo nuevo
        if(imagenArchivo!=null && !imagenArchivo.isEmpty()){
            String nombreArchivo = guardarImagenLocal(imagenArchivo);
            producto.setImagen("/uploads/productos/" + nombreArchivo);
        }
        return productoRepository.save(producto);
    }

    //buscamos coincidencias en el nombre sin distinguir mayúsculas de minúsculas
    public List<Producto> buscarProductosPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    //convertimos el producto encontrado en una lista, si no existe devolvemos una lista vacía
    public List<Producto> buscarProductoPorId(Long idProducto) {
        return productoRepository.findById(idProducto).map(List::of).orElseGet(List::of);
    }

}
