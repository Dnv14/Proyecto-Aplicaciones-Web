package com.example.ProyectoAplicacionesWeb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class webConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registri){
        Path uploadDir = Paths.get("sec/main/resources/static/uploads/productos");
        String uploadDirPath = uploadDir.toFile().getAbsolutePath();
        registri.addResourceHandler("/productos/**").addResourceLocations("file:"+uploadDirPath + "/")                ;
    }

}
