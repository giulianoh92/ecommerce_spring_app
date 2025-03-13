package com.ecommerce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ecommerce.desktop.controller.SwingController;
import com.ecommerce.main.config.database.DatabaseConnection;

import java.awt.GraphicsEnvironment;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    private SwingController swingController;

    @Autowired
    private DatabaseConnection databaseConnection;

    public static void main(String[] args) {
        // setear la propiedad java.awt.headless a false para que se pueda mostrar la interfaz gráfica
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(App.class, args);
        System.out.println("Documentación Swagger disponible en: http://localhost:8080/swagger-ui/index.html");
    }

    @Override
    public void run(String... args) throws Exception {
        boolean launchUI = false;
        for (String arg : args) {
            if (arg.equalsIgnoreCase("--ui")) {
                launchUI = true;
                break;
            }
        }

        if (launchUI && !GraphicsEnvironment.isHeadless()) {
            databaseConnection.connect(); // conectar a la base de datos
            swingController.initializeUI(); // lanzar la interfaz gráfica
        } else {
            System.out.println("La API está corriendo en segundo plano. Para lanzar la interfaz gráfica, ejecute el programa con el argumento --ui");
        }
    }
}