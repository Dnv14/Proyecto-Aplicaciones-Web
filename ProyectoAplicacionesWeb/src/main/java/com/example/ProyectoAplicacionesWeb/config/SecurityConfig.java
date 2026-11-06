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

    //registramos la configuración de seguridad para que Spring la utilice
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //definimos quién puede acceder a cada ruta
                .authorizeHttpRequests(auth -> auth
                        //permitimos abrir el login, el registro, los estilos y las imágenes sin iniciar sesión
                        .requestMatchers("/login", "/registro" , "/styles/**", "/imgs/**").permitAll()
                        //permitimos entrar a las rutas de administración solamente con el rol ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        //permitimos entrar a las rutas del cliente solamente con el rol CLIENTE
                        .requestMatchers("/cliente/**").hasRole("CLIENTE")
                        //dejamos públicas las demás rutas
                        .anyRequest().permitAll()
                )
                //configuramos el inicio de sesión mediante el formulario
                .formLogin(login -> login
                        //indicamos la dirección donde se muestra el formulario
                        .loginPage("/login")
                        //indicamos la dirección que recibe los datos del formulario por POST
                        .loginProcessingUrl("/login")
                        //indicamos el nombre del input que contiene el correo
                        .usernameParameter("correo")
                        //indicamos el nombre del input que contiene la contraseña
                        .passwordParameter("contrasenia")
                        //ejecutamos esta parte cuando las credenciales son correctas
                        .successHandler((request, response,authentication) ->{
                            //comprobamos si el usuario tiene el permiso ROLE_ADMIN
                            boolean esAdmin=authentication.getAuthorities().stream().anyMatch(p -> p.getAuthority().equals("ROLE_ADMIN"));
                            //elegimos la página de destino según el rol del usuario
                            String destino=esAdmin?"/admin":"/cliente";
                            //enviamos al navegador a la página elegida
                            response.sendRedirect(request.getContextPath()+destino);
                        })
                        //si las credenciales fallan volvemos al login con el parámetro de error
                        .failureUrl("/login?error")
                        //permitimos acceder a las rutas del login sin estar autenticado
                        .permitAll()
                )
                //configuramos el cierre de sesión
                .logout(logout -> logout
                        //indicamos la dirección que recibe el POST para cerrar sesión
                        .logoutUrl("/logout")
                        //después de salir volvemos al login
                        .logoutSuccessUrl("/login?logout")
                        //invalidamos la sesión HTTP y eliminamos sus datos
                        .invalidateHttpSession(true)
                        //permitimos acceder a las rutas de cierre de sesión
                        .permitAll()
                );
        //construimos y devolvemos los filtros con las reglas configuradas
        return http.build();
    }

    @Bean
    //registramos BCrypt para generar hashes de las contraseñas y comprobarlas al iniciar sesión
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}