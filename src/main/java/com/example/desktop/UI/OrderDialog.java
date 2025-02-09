package com.example.desktop.UI;

import com.example.main.services.Orders.dto.OrderUpdateDTO;
import com.example.main.services.Orders.dto.OrderGetDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class OrderDialog extends JDialog {
    private JComboBox<String> statusComboBox;
    private Map<Long, String> statuses;
    private boolean confirmed;

    public OrderDialog(OrderGetDTO order, Map<Long, String> statuses) {
        this.statuses = statuses;
        statuses.forEach((id, status) -> System.out.println(id + " " + status));
        setTitle("Edit Order Status");
        setModal(true);
        setSize(400, 200);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Status:"));
        statusComboBox = new JComboBox<>(statuses.values().toArray(new String[0]));
        if (order != null) {
            statusComboBox.setSelectedItem(order.getStatus());
        }
        add(statusComboBox);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = true;
                setVisible(false);
            }
        });
        add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                setVisible(false);
            }
        });
        add(cancelButton);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public OrderUpdateDTO getOrderUpdateDTO() {
        Long statusId = statuses.entrySet().stream()
                .filter(entry -> entry.getValue().equals(statusComboBox.getSelectedItem()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        return new OrderUpdateDTO(statusId);
    }
}