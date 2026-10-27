package com.example.ProyectoAplicacionesWeb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //registra la configuracion de seguridad para que spring la utilice
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //define quien puede acceder a cada ruta
                .authorizeHttpRequests(auth -> auth
                        //permite abrir el login, los estilos, y las imagenes sin iniciar sesion
                        .requestMatchers("/admin/login", "/styles/**", "/imgs/**").permitAll()
                        //solo permite entrar al panel a los ususarios con el rol de admin
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        //deja punlicas las demas rutas
                        .anyRequest().permitAll()
                )
                //configuramos el inicio de sesion mediante el formulario
                .formLogin(login -> login
                        //direccion donde el controlador mostrara el formulario
                        .loginPage("/admin/login")
                        //direccion que  recibe los datos del formulario por post
                        .loginProcessingUrl("/admin/login")
                        //nombre del intput que tiene el correo
                        .usernameParameter("correo")
                        //nombre del intput que tiene la contraseña
                        .passwordParameter("contrasenia")
                        //ejecuta cuando las credenciales sean correctas
                        .successHandler((request, response,authentication) ->{
                            //comprubea si alguno de sus permisos es role_admin
                            boolean esAdmin=authentication.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ROLE_ADMIN"));
                            //elige el destino segun su rol de ususario
                            String destino=esAdmin?"/admin":"/cliente";
                            //envia al navegador al destino elegido
                            response.sendRedirect(request.getContextPath()+destino);
                        })
                        //si las credenciales fallan vuelve al login con el parametro error
                        .failureUrl("/admin/login?error")
                        //permite acceder a las rutas del login sin estar autenticado
                        .permitAll()
                )
                //configuracion del cierre de sesion
                .logout(logout -> logout
                        //direccion a la que se enviara un post para cerrar sesion
                        .logoutUrl("/admin/logout")
                        //despues de salir vuelve al lgin
                        .logoutSuccessUrl("/admin/login?logout")
                        //invalida la sesion http y elimina sus datos
                        .invalidateHttpSession(true)
                        //permitimos acceder a las rutas de cierre de sesion
                        .permitAll()
                );
        //construye y devuelve la cadena de filtros con esas reglas
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}