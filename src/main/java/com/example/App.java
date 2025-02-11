package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.desktop.controller.SwingController;
import com.example.main.config.database.DatabaseConnection;

import java.awt.GraphicsEnvironment;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    private SwingController swingController;

    @Autowired
    private DatabaseConnection databaseConnection;

    public static void main(String[] args) {
        // Set the headless property to false
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(App.class, args);
        System.out.println("Swagger documentation available at: http://localhost:8080/swagger-ui/index.html");
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
            databaseConnection.connect();
            swingController.initializeUI();
        } else {
            System.out.println("API is running. Swing GUI will not be displayed.");
        }
    }
}