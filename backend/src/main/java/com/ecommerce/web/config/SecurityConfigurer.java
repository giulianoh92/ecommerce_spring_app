package com.ecommerce.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.ecommerce.main.services.Users.UserService;
import com.ecommerce.web.filter.JwtRequestFilter;

import java.util.Arrays;


/**
 * Clase de configuración para la seguridad de la aplicación
 * permite la comunicación con el frontend y la autenticación de los usuarios
 * además de la configuración de los filtros de seguridad
 * 
 * define ruta de acceso a los endpoints de la aplicación
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigurer {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    UserDetailsService userDetailsService() {
        return userService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Arrays.asList("*"));
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(Arrays.asList("*"));
                    return config;
                }))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/users/login", "/users/register", "/swagger-ui/**", "/v3/api-docs/**", "/populate").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}