package com.example.desktop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.example.desktop.UI.AdminPanel;
import com.example.main.services.ServiceContainer;

@Component
public class SwingController {
    
    @Autowired
    private ServiceContainer serviceContainer;

    public void initializeUI() {
        JFrame frame = new JFrame("Admin Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel adminPanel = new AdminPanel(serviceContainer);
        frame.add(adminPanel);

        frame.setVisible(true);
    }
}