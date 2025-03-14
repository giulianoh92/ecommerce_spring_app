package com.ecommerce;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class App implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        System.out.println("Documentación Swagger disponible en: http://localhost:8080/swagger-ui/index.html");
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Servidor correindo en: http://localhost:8080");
    }
}