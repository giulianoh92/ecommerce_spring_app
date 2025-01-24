package com.example.main.config.database;

import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnection {

    private SessionFactory sessionFactory;

    public void connect() {
        try {
            // Cargar las credenciales del archivo .env
            Dotenv dotenv = Dotenv.load();
            String url = dotenv.get("DB_URL");
            String username = dotenv.get("DB_USERNAME");
            String password = dotenv.get("DB_PASSWORD");

            // Configuración de Hibernate
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.connection.url", url);
            configuration.setProperty("hibernate.connection.username", username);
            configuration.setProperty("hibernate.connection.password", password);
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");

            // Añadir paquetes de modelos
            configuration.addPackage("com.example.main.domain.orm.Items");
            configuration.addPackage("com.example.main.domain.orm.Orders");
            configuration.addPackage("com.example.main.domain.orm.PaymentMethods");
            configuration.addPackage("com.example.main.domain.orm.Payments");
            configuration.addPackage("com.example.main.domain.orm.ProductCategories");
            configuration.addPackage("com.example.main.domain.orm.Products");
            configuration.addPackage("com.example.main.domain.orm.Roles");
            configuration.addPackage("com.example.main.domain.orm.Users");

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