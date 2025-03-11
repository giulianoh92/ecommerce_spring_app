package com.ecommerce.desktop.UI;

import javax.swing.*;

import com.ecommerce.main.services.Orders.dto.OrderGetDTO;
import com.ecommerce.main.services.Orders.dto.OrderUpdateDTO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class OrderDialog extends JDialog {
    private JComboBox<String> statusComboBox;
    private Map<Long, String> statuses;
    private boolean confirmed;

    // Constructor para inicializar el diálogo de pedido
    public OrderDialog(OrderGetDTO order, Map<Long, String> statuses) {
        this.statuses = statuses;
        statuses.forEach((id, status) -> System.out.println(id + " " + status));
        setTitle("Cambiar estado del pedido");
        setModal(true);
        setSize(400, 200);
        setLayout(new GridLayout(3, 2));

        // ComboBox para seleccionar el estado del pedido
        add(new JLabel("Estado:"));
        statusComboBox = new JComboBox<>(statuses.values().toArray(new String[0]));
        if (order != null) {
            statusComboBox.setSelectedItem(order.getStatus());
        }
        add(statusComboBox);

        // Botón para confirmar la acción
        JButton confirmButton = new JButton("Confirmar");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = true;
                setVisible(false);
            }
        });
        add(confirmButton);

        // Botón para cancelar la acción
        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                setVisible(false);
            }
        });
        add(cancelButton);
    }

    // Método para verificar si la acción fue confirmada
    public boolean isConfirmed() {
        return confirmed;
    }

    // Método para obtener los datos del pedido a actualizar
    public OrderUpdateDTO getOrderUpdateDTO() {
        Long statusId = statuses.entrySet().stream()
                .filter(entry -> entry.getValue().equals(statusComboBox.getSelectedItem()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        return new OrderUpdateDTO(statusId);
    }
}