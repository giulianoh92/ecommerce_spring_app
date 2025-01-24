package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.main.config.database.DatabaseConnection;

import java.awt.GraphicsEnvironment;

@SpringBootApplication
public class App implements CommandLineRunner {

    //@Autowired
    //private SwingController swingController;

    @Autowired
    private DatabaseConnection databaseConnection;

    public static void main(String[] args) {
        // Set the headless property to false
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (!GraphicsEnvironment.isHeadless()) {
            databaseConnection.connect();
            //swingController.createAndShowGUI();
        } else {
            System.out.println("Headless environment detected. Swing GUI will not be displayed.");
        }
    }
}