package com.ecommerce.desktop.UI;

import javax.swing.*;

import com.ecommerce.main.services.Products.dto.ProductCreateDTO;
import com.ecommerce.main.services.Products.dto.ProductGetDTO;
import com.ecommerce.main.services.Products.dto.ProductUpdateDTO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ProductDialog extends JDialog {
    private JTextField nameField;
    private JTextField descriptionField;
    private JTextField priceField;
    private JTextField stockField;
    private JComboBox<String> categoryComboBox;
    private Map<Long, String> categories;
    private boolean confirmed;
    private Validator validator;

    // Constructor para inicializar el diálogo de producto
    public ProductDialog(ProductGetDTO product, Map<Long, String> categories) {
        this.categories = categories;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        setTitle(product == null ? "Agregar Producto" : "Editar Producto");
        setModal(true);
        setSize(400, 300);
        setLayout(new GridLayout(6, 2));

        // Campo de texto para el nombre del producto
        add(new JLabel("Nombre:"));
        nameField = new JTextField(product != null ? product.getName() : "");
        add(nameField);

        // Campo de texto para la descripción del producto
        add(new JLabel("Descripción:"));
        descriptionField = new JTextField(product != null ? product.getDescription() : "");
        add(descriptionField);

        // Campo de texto para el precio unitario del producto
        add(new JLabel("Precio unitario:"));
        priceField = new JTextField(product != null ? String.valueOf(product.getUnitPrice()) : "");
        add(priceField);

        // Campo de texto para el stock del producto
        add(new JLabel("Stock:"));
        stockField = new JTextField(product != null ? String.valueOf(product.getStock()) : "");
        add(stockField);

        // ComboBox para seleccionar la categoría del producto
        add(new JLabel("Categoría:"));
        categoryComboBox = new JComboBox<>(categories.values().toArray(new String[0]));
        if (product != null) {
            categoryComboBox.setSelectedItem(product.getCategory());
        }
        add(categoryComboBox);

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

    // Método para obtener los datos del producto a crear
    public ProductCreateDTO getProductCreateDTO() {
        Long categoryId = categories.entrySet().stream()
                .filter(entry -> entry.getValue().equals(categoryComboBox.getSelectedItem()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    
        try {
            return new ProductCreateDTO(
                    nameField.getText(),
                    descriptionField.getText(),
                    Double.parseDouble(priceField.getText()),
                    Integer.parseInt(stockField.getText()),
                    null,
                    categoryId
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese números válidos para el precio y el stock.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Método para obtener los datos del producto a actualizar
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

    // Método para validar el DTO del producto
    public <T> Set<ConstraintViolation<T>> validateDTO(T dto) {
        return validator.validate(dto);
    }
}