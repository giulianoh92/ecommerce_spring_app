package com.example.desktop.UI;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import com.example.main.controllers.MainController;

public class AdminPanel extends JPanel {

    private MainController mainController;

    public AdminPanel(MainController mainController) {
        this.mainController = mainController;
        initializeComponents();
    }

    private void initializeComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel pedidosPanel = new TabPanel<>(mainController::getAllOrders, new String[]{"ID", "User ID", "Status", "Total"}, 
            order -> new Object[]{order.getId(), order.getId(), order.getStatus(), order.getTotal()});

        JPanel productosPanel = new TabPanel<>(mainController::getAllProducts, new String[]{"ID", "Name", "Category", "Price"}, 
            product -> new Object[]{product.getId(), product.getName(), product.getCategory(), product.getUnitPrice()}); // Update consumer for products

        JPanel clientesPanel = new TabPanel<>(mainController::getAllUsers, new String[]{"ID", "Name", "Email"}, 
            user -> new Object[]{user.getId(), user.getFirstName() + ' ' + user.getLastName(), user.getEmail()}); // Update consumer for users

        tabbedPane.addTab("Pedidos", pedidosPanel);
        tabbedPane.addTab("Productos", productosPanel);
        tabbedPane.addTab("Clientes", clientesPanel);

        this.add(tabbedPane);
    }
}