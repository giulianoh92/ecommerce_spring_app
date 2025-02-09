package com.example.desktop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.desktop.UI.AdminPanel;
import com.example.desktop.UI.ClientTableModel;
import com.example.desktop.UI.OrderTableModel;
import com.example.desktop.UI.ProductTableModel;
import com.example.main.services.ServiceContainer;
import com.example.main.services.Orders.dto.OrderGetDTO;
import com.example.main.services.Products.dto.ProductGetDTO;
import com.example.main.services.Users.dto.UserGetDTO;

import javax.swing.*;
import java.util.List;

@Component
public class SwingController {
    
    @Autowired
    private ServiceContainer serviceContainer;

    public void initializeUI() {
        AdminPanel adminPanel = new AdminPanel();

        // Obtener listas de objetos desde ServiceContainer
        List<ProductGetDTO> products = serviceContainer.productsService.getAll();
        List<UserGetDTO> users = serviceContainer.userService.getAll();
        List<OrderGetDTO> orders = serviceContainer.ordersService.getAll();

        // Crear tablas con los datos obtenidos
        JTable productsTable = new JTable(new ProductTableModel(products));
        JTable usersTable = new JTable(new ClientTableModel(users));
        JTable ordersTable = new JTable(new OrderTableModel(orders));

        // Añadir tablas a los paneles correspondientes
        adminPanel.getProductsPanel().add(new JScrollPane(productsTable));
        adminPanel.getUsersPanel().add(new JScrollPane(usersTable));
        adminPanel.getOrdersPanel().add(new JScrollPane(ordersTable));
    }
}