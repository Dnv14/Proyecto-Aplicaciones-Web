package com.example.ProyectoAplicacionesWeb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class webConfig implements WebMvcConfigurer {

    @Override
    //permitimos consultar desde el navegador las imágenes guardadas en la carpeta de productos
    public void addResourceHandlers(ResourceHandlerRegistry registri){
        Path uploadDir = Paths.get("src/main/resources/static/uploads/productos");
        //convertimos la carpeta a una ruta absoluta del equipo
        String uploadDirPath = uploadDir.toFile().getAbsolutePath();
        //relacionamos la URL de imágenes con la carpeta física donde se guardan
        registri.addResourceHandler("/uploads/productos/**").addResourceLocations("file:" + uploadDirPath + "/");
    }

}
