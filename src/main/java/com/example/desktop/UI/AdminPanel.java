package com.example.desktop.UI;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.example.main.services.ServiceContainer;

public class AdminPanel extends JPanel {

    private ServiceContainer serviceContainer;

    public AdminPanel(ServiceContainer serviceContainer) {
        this.serviceContainer = serviceContainer;
        initializeComponents();
    }

    private void initializeComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel pedidosPanel = new TabPanel<>(serviceContainer.ordersService::getAll);
        JPanel productosPanel = new TabPanel<>(serviceContainer.productsService::getAll);
        JPanel clientesPanel = new TabPanel<>(serviceContainer.userService::getAll);

        tabbedPane.addTab("Pedidos", pedidosPanel);
        tabbedPane.addTab("Productos", productosPanel);
        tabbedPane.addTab("Clientes", clientesPanel);

        this.add(tabbedPane);
    }
}