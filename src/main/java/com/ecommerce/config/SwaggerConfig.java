package com.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/***
 * Clase de configuración para la documentación de la API
 * permite la generación de la documentación de la API en formato OpenAPI
 */
@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Ecommerce")
                .version("1.0")
            );
    }
}