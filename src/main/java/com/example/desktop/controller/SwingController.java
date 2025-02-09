package com.example.desktop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.desktop.UI.AdminPanel;
import com.example.desktop.UI.ClientTableModel;
import com.example.desktop.UI.OrderTableModel;
import com.example.desktop.UI.ProductTableModel;
import com.example.desktop.UI.TabWithTable;
import com.example.main.services.ServiceContainer;
import com.example.main.services.Orders.dto.OrderGetDTO;
import com.example.main.services.Products.dto.ProductGetDTO;
import com.example.main.services.Users.dto.UserGetDTO;

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

        // Crear paneles con tablas
        TabWithTable<ProductGetDTO> productsTab = new TabWithTable<>(new ProductTableModel());
        productsTab.setData(products);
        TabWithTable<UserGetDTO> usersTab = new TabWithTable<>(new ClientTableModel());
        usersTab.setData(users);
        TabWithTable<OrderGetDTO> ordersTab = new TabWithTable<>(new OrderTableModel());
        ordersTab.setData(orders);

        // Añadir paneles a los paneles correspondientes
        adminPanel.getProductsPanel().add(productsTab);
        adminPanel.getUsersPanel().add(usersTab);
        adminPanel.getOrdersPanel().add(ordersTab);
    }
}