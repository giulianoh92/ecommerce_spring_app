package com.swingapp;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mi Aplicación Swing con Maven");
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(null);

            JLabel label = new JLabel("¡Hola, Mundo!");
            label.setBounds(150, 100, 100, 30);
            frame.add(label);

            frame.setVisible(true);
        });
    }
}

