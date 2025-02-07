package com.example.desktop.UI;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.example.main.services.ServiceContainer;
import com.example.main.domain.models.Products;
import com.example.main.domain.models.Users;
import com.example.main.domain.models.Orders;

public class AdminPanel extends JPanel {

    private ServiceContainer serviceContainer;

    public AdminPanel(ServiceContainer serviceContainer) {
        this.serviceContainer = serviceContainer;
        initializeComponents();
    }

    private void initializeComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel pedidosPanel = new TabPanel<>(serviceContainer.ordersService::getAllEntity, new String[]{"ID", "User ID", "Status", "Total"}, 
            order -> new Object[]{order.getId(), order.getUser().getId(), order.getStatus().getName(), order.getTotal()});

        JPanel productosPanel = new TabPanel<>(serviceContainer.productsService::getAllEntity, new String[]{"ID", "Name", "Description", "Category", "Price", "Stock"}, 
            product -> new Object[]{product.getId(), product.getName(), product.getDescription(), product.getCategory().getName(), product.getUnitPrice(), product.getStock()});

        JPanel clientesPanel = new TabPanel<>(serviceContainer.userService::getAllEntity, new String[]{"ID", "Name", "Email", "Address", "Phone"}, 
            user -> new Object[]{user.getId(), user.getFirstName() + ' ' + user.getLastName(), user.getEmail(), user.getAddress(), user.getPhoneNumber()});

        tabbedPane.addTab("Pedidos", pedidosPanel);
        tabbedPane.addTab("Productos", productosPanel);
        tabbedPane.addTab("Clientes", clientesPanel);

        this.add(tabbedPane);
    }
}