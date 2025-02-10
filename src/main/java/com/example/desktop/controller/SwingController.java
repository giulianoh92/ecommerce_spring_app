package com.example.desktop.controller;

import com.example.desktop.UI.EntityTab;
import com.example.desktop.UI.OrderDialog;
import com.example.desktop.UI.ProductDialog;
import com.example.main.error.CustomError;
import com.example.main.services.ServiceContainer;
import com.example.main.services.Orders.dto.OrderGetDTO;
import com.example.main.services.Orders.dto.OrderUpdateDTO;
import com.example.main.services.Products.dto.ProductCreateDTO;
import com.example.main.services.Products.dto.ProductGetDTO;
import com.example.main.services.Products.dto.ProductUpdateDTO;
import com.example.main.services.Users.dto.UserGetDTO;
import com.example.main.domain.models.Categories;
import com.example.main.domain.models.Statuses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;

@Component
public class SwingController {
    
    @Autowired
    private ServiceContainer serviceContainer;

    private List<ProductGetDTO> products;
    private List<OrderGetDTO> orders;
    private List<UserGetDTO> users;

    public void initializeUI() {
        // Inicializar las listas
        products = Collections.emptyList();
        orders = Collections.emptyList();
        users = Collections.emptyList();
    
        JFrame frame = new JFrame("Panel de Administracion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
    
        JTabbedPane tabbedPane = new JTabbedPane();
    
        String[] userColumnNames = {"ID", "Nombre", "Apellido", "Email", "Direccion", "Telefono"};
        String[] productColumnNames = {"ID", "Nombre", "Descripcion", "Precio", "Stock", "Categoria"};
        String[] orderColumnNames = {"ID", "ID del cliente", "Estado", "Total"};
    
        EntityTab<UserGetDTO> usersTab = new EntityTab<>(user -> new Object[]{user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getAddress(), user.getPhoneNumber()}, userColumnNames, null);
        EntityTab<ProductGetDTO> productsTab = new EntityTab<>(product -> new Object[]{product.getId(), product.getName(), product.getDescription(), product.getUnitPrice(), product.getStock(), product.getCategory()}, productColumnNames, null);
        productsTab.getTable().addMouseListener(createProductMouseListener(productsTab));
        EntityTab<OrderGetDTO> ordersTab = new EntityTab<>(order -> new Object[]{order.getId(), order.getUserId(), order.getStatus(), order.getTotal()}, orderColumnNames, null);
        ordersTab.getTable().addMouseListener(createOrderMouseListener(ordersTab));
    
        // Add refresh buttons
        JButton refreshUsersButton = new JButton("Actualizar");
        refreshUsersButton.addActionListener(e -> refreshUsersTab(usersTab));
        JPanel usersPanel = new JPanel(new BorderLayout());
        usersPanel.add(refreshUsersButton, BorderLayout.NORTH);
        usersPanel.add(new JScrollPane(usersTab.getTable()), BorderLayout.CENTER);
    
        JButton refreshProductsButton = new JButton("Actualizar");
        refreshProductsButton.addActionListener(e -> refreshProductsTab(productsTab));
        JButton addProductButton = new JButton("Agregar Producto");
        addProductButton.addActionListener(e -> openAddProductDialog(productsTab));
        JPanel productsPanel = new JPanel(new BorderLayout());
        JPanel productsButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        productsButtonPanel.add(refreshProductsButton);
        productsButtonPanel.add(addProductButton);
        productsPanel.add(productsButtonPanel, BorderLayout.NORTH);
        productsPanel.add(new JScrollPane(productsTab.getTable()), BorderLayout.CENTER);
    
        JButton refreshOrdersButton = new JButton("Actualizar");
        refreshOrdersButton.addActionListener(e -> refreshOrdersTab(ordersTab));
        JPanel ordersPanel = new JPanel(new BorderLayout());
        ordersPanel.add(refreshOrdersButton, BorderLayout.NORTH);
        ordersPanel.add(new JScrollPane(ordersTab.getTable()), BorderLayout.CENTER);
    
        tabbedPane.add("Clientes", usersPanel);
        tabbedPane.add("Productos", productsPanel);
        tabbedPane.add("Pedidos", ordersPanel);
    
        frame.add(tabbedPane);
        frame.setVisible(true);
    
        refreshTabs(usersTab, productsTab, ordersTab);
    }

    private MouseAdapter createOrderMouseListener(EntityTab<OrderGetDTO> ordersTab) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable table = (JTable) e.getSource();
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        Long id = (Long) table.getValueAt(row, 0);
                        OrderGetDTO order = orders.stream().filter(o -> o.getId() == id).findFirst().orElse(null);
                        System.out.println(orders.get(0));
                        if (order != null) {
                            openEditOrderDialog(order, ordersTab);
                        }
                    }
                }
            }
        };
    }

    private void openEditOrderDialog(OrderGetDTO order, EntityTab<OrderGetDTO> ordersTab) {
        List<Statuses> statusesList = serviceContainer.ordersService.getAllStatuses();
        Map<Long, String> statuses = convertStatusesToMap(statusesList);
        OrderDialog dialog = new OrderDialog(order, statuses);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            try {
                OrderUpdateDTO dto = dialog.getOrderUpdateDTO();
                serviceContainer.ordersService.update(order.getId(), dto);
                refreshOrdersTab(ordersTab);
            } catch (CustomError e) {
                JOptionPane.showMessageDialog(dialog, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(dialog, "Ocurrio un error al actualizar el estado del pedido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshOrdersTab(EntityTab<OrderGetDTO> ordersTab) {
        try {
            orders = serviceContainer.ordersService.getAll();
            ordersTab.refresh(orders);
        } catch (CustomError e) {
            ordersTab.refresh(Collections.emptyList());
        }
    }

    private MouseAdapter createProductMouseListener(EntityTab<ProductGetDTO> productsTab) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable table = (JTable) e.getSource();
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        Long id = (Long) table.getValueAt(row, 0);
                        ProductGetDTO product = products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
                        System.out.println(product);
                        if (product != null) {
                            openEditProductDialog(product, productsTab);
                        }
                    }
                }
            }
        };
    }

    private void openAddProductDialog(EntityTab<ProductGetDTO> productsTab) {
        List<Categories> categoriesList = serviceContainer.productsService.getAllCategories();
        Map<Long, String> categories = convertCategoriesToMap(categoriesList);
        ProductDialog dialog = new ProductDialog(null, categories);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            ProductCreateDTO dto = dialog.getProductCreateDTO();
            Set<ConstraintViolation<ProductCreateDTO>> violations = dialog.validateDTO(dto);
            if (!violations.isEmpty()) {
                showValidationErrors(violations);
                return;
            }
            try {
                serviceContainer.productsService.create(dto);
                refreshProductsTab(productsTab);
            } catch (CustomError e) {
                JOptionPane.showMessageDialog(dialog, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(dialog, "Por favor ingrese numeros validos para el precio y el stock.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(dialog, "Ocurrio un error al agregar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openEditProductDialog(ProductGetDTO product, EntityTab<ProductGetDTO> productsTab) {
        List<Categories> categoriesList = serviceContainer.productsService.getAllCategories();
        Map<Long, String> categories = convertCategoriesToMap(categoriesList);
        ProductDialog dialog = new ProductDialog(product, categories);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            ProductUpdateDTO dto = dialog.getProductUpdateDTO();
            Set<ConstraintViolation<ProductUpdateDTO>> violations = dialog.validateDTO(dto);
            if (!violations.isEmpty()) {
                showValidationErrors(violations);
                return;
            }
            try {
                serviceContainer.productsService.update(product.getId(), dto);
                refreshProductsTab(productsTab);
            } catch (CustomError e) {
                JOptionPane.showMessageDialog(dialog, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(dialog, "Por favor ingrese numeros validos para el precio y el stock.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(dialog, "Ocurrio un error al actualizar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshTabs(EntityTab<UserGetDTO> usersTab, EntityTab<ProductGetDTO> productsTab, EntityTab<OrderGetDTO> ordersTab) {

        refreshUsersTab(usersTab);

        refreshProductsTab(productsTab);

        refreshOrdersTab(ordersTab);
    }

    private void refreshProductsTab(EntityTab<ProductGetDTO> productsTab) {
        try {
            products = serviceContainer.productsService.getAll();
            productsTab.refresh(products);
        } catch (CustomError e) {
            productsTab.refresh(Collections.emptyList());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error al obtener los productos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshUsersTab(EntityTab<UserGetDTO> usersTab) {
        try {
            users = serviceContainer.userService.getAll();
            usersTab.refresh(users);
        } catch (CustomError e) {
            usersTab.refresh(Collections.emptyList());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error al obtener los usuarios.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showValidationErrors(Set<? extends ConstraintViolation<?>> violations) {
        StringBuilder errorMessage = new StringBuilder("Errores de validación:\n");
        for (ConstraintViolation<?> violation : violations) {
            errorMessage.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("\n");
        }
        JOptionPane.showMessageDialog(null, errorMessage.toString(), "Errores de validación", JOptionPane.ERROR_MESSAGE);
    }

    private Map<Long, String> convertStatusesToMap(List<Statuses> statusesList) {
        return statusesList.stream()
                .collect(Collectors.toMap(Statuses::getId, Statuses::getName));
    }

    private Map<Long, String> convertCategoriesToMap(List<Categories> categoriesList) {
        return categoriesList.stream()
                .collect(Collectors.toMap(Categories::getId, Categories::getName));
    }
}