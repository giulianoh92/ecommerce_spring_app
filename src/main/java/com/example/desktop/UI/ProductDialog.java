package com.example.desktop.UI;

import com.example.main.services.Products.dto.ProductCreateDTO;
import com.example.main.services.Products.dto.ProductGetDTO;
import com.example.main.services.Products.dto.ProductUpdateDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class ProductDialog extends JDialog {
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField priceField;
    private JTextField stockField;
    private JComboBox<String> categoryComboBox;
    private Map<Long, String> categories;
    private boolean confirmed;

    public ProductDialog(ProductGetDTO product, Map<Long, String> categories) {
        this.categories = categories;
        setTitle(product == null ? "Add Product" : "Edit Product");
        setModal(true);
        setSize(400, 300);
        setLayout(new GridLayout(6, 2));

        add(new JLabel("Name:"));
        nameField = new JTextField(product != null ? product.getName() : "");
        add(nameField);

        add(new JLabel("Description:"));
        descriptionField = new JTextField(product != null ? product.getDescription() : "");
        add(descriptionField);

        add(new JLabel("Price:"));
        priceField = new JTextField(product != null ? String.valueOf(product.getUnitPrice()) : "");
        add(priceField);

        add(new JLabel("Stock:"));
        stockField = new JTextField(product != null ? String.valueOf(product.getStock()) : "");
        add(stockField);

        add(new JLabel("Category:"));
        categoryComboBox = new JComboBox<>(categories.values().toArray(new String[0]));
        if (product != null) {
            categoryComboBox.setSelectedItem(product.getCategory());
        }
        add(categoryComboBox);

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

    public ProductCreateDTO getProductCreateDTO() {
        Long categoryId = categories.entrySet().stream()
                .filter(entry -> entry.getValue().equals(categoryComboBox.getSelectedItem()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        return new ProductCreateDTO(
                nameField.getText(),
                descriptionField.getText(),
                Double.parseDouble(priceField.getText()),
                Integer.parseInt(stockField.getText()),
                null,
                categoryId
        );
    }

    public ProductUpdateDTO getProductUpdateDTO() {
        Long categoryId = categories.entrySet().stream()
                .filter(entry -> entry.getValue().equals(categoryComboBox.getSelectedItem()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        return new ProductUpdateDTO(
                nameField.getText(),
                descriptionField.getText(),
                Double.parseDouble(priceField.getText()),
                Integer.parseInt(stockField.getText()),
                null,
                categoryId
        );
    }
}