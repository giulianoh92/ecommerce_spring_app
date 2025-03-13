package com.swingapp;

import com.swingapp.controllers.HttpController;
import com.swingapp.models.HttpResponseData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("HTTP Client Swing App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JTextField routeField = new JTextField(20);
        JButton sendButton = new JButton("SEND");
        JTextArea responseArea = new JTextArea(10, 30);
        responseArea.setEditable(false);

        panel.add(new JLabel("Route:"));
        panel.add(routeField);
        panel.add(sendButton);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(responseArea), BorderLayout.CENTER);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String route = routeField.getText();
                if (!route.isEmpty()) {
                    HttpController httpController = new HttpController();
                    HttpResponseData data = httpController.fetchDataFromApi(route);
                    if (data != null) {
                        StringBuilder responseText = new StringBuilder();
                        responseText.append("Status Code: ").append(data.getStatusCode()).append("\n");
                        responseText.append("Headers: ").append(data.getHeaders()).append("\n");
                        responseText.append("Body: ").append(data.getBody()).append("\n");
                        responseArea.setText(responseText.toString());
                    } else {
                        responseArea.setText("Failed to fetch data");
                    }
                } else {
                    responseArea.setText("Please enter a valid route.");
                }
            }
        });

        frame.setVisible(true);
    }
}