package com.example.desktop.UI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class AdminPanel extends JFrame {

    private JTabbedPane tabbedPane;
    private JPanel productsPanel;
    private JPanel usersPanel;
    private JPanel ordersPanel;

    public AdminPanel() {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Admin Panel");
        tabbedPane = new JTabbedPane();
        productsPanel = new JPanel();
        usersPanel = new JPanel();
        ordersPanel = new JPanel();
        tabbedPane.addTab("Products", productsPanel);
        tabbedPane.addTab("Users", usersPanel);
        tabbedPane.addTab("Orders", ordersPanel);
        this.add(tabbedPane);
        this.setVisible(true);
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public JPanel getProductsPanel() {
        return productsPanel;
    }

    public JPanel getUsersPanel() {
        return usersPanel;
    }

    public JPanel getOrdersPanel() {
        return ordersPanel;
    }
    
}
