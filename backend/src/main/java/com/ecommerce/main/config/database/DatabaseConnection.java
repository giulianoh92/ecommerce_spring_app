package com.ecommerce.main.config.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnection {

    private SessionFactory sessionFactory;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String dbDriver;

    public void connect() {
        try {
            // Configuración de Hibernate
            Configuration configuration = new Configuration();

            // Configurar las propiedades de conexión
            configuration.setProperty("hibernate.connection.url", dbUrl);
            configuration.setProperty("hibernate.connection.username", dbUsername);
            configuration.setProperty("hibernate.connection.password", dbPassword);
            configuration.setProperty("hibernate.connection.driver_class", dbDriver);

            // Añadir paquetes de modelos
            configuration.addPackage("com.example.main.domain.models");

            sessionFactory = configuration.buildSessionFactory();

            // Probar la conexión
            try (Session session = sessionFactory.openSession()) {
                System.out.println("Conexión exitosa a la base de datos");
            }
        } catch (Exception e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    public void disconnect() {
        if (sessionFactory != null) {
            sessionFactory.close();
            System.out.println("Conexión cerrada");
        }
    }
}