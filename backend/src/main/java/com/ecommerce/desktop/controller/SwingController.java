package com.ecommerce.desktop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ecommerce.desktop.UI.EntityTab;
import com.ecommerce.desktop.UI.OrderDialog;
import com.ecommerce.desktop.UI.ProductDialog;
import com.ecommerce.main.domain.models.Categories;
import com.ecommerce.main.domain.models.Statuses;
import com.ecommerce.main.error.CustomError;
import com.ecommerce.main.services.ServiceContainer;
import com.ecommerce.main.services.Orders.dto.OrderGetDTO;
import com.ecommerce.main.services.Orders.dto.OrderUpdateDTO;
import com.ecommerce.main.services.Products.dto.ProductCreateDTO;
import com.ecommerce.main.services.Products.dto.ProductGetDTO;
import com.ecommerce.main.services.Products.dto.ProductUpdateDTO;
import com.ecommerce.main.services.Users.dto.UserGetDTO;

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

    private int currentPage = 1;
    private final int pageSize = 10;

    private JButton nextPageButton;

    public void initializeUI() {
        // Inicializar las listas
        products = Collections.emptyList();
        orders = Collections.emptyList();
        users = Collections.emptyList();
    
        // Crear el marco principal de la aplicación
        JFrame frame = new JFrame("Panel de Administracion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
    
        // Crear el panel de pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
    
        // Definir los nombres de las columnas para cada entidad
        String[] userColumnNames = {"ID", "Nombre", "Apellido", "Email", "Direccion", "Telefono"};
        String[] productColumnNames = {"ID", "Nombre", "Descripcion", "Precio", "Stock", "Categoria"};
        String[] orderColumnNames = {"ID", "ID del cliente", "Estado", "Total"};
    
        // Crear las pestañas para cada entidad
        EntityTab<UserGetDTO> usersTab = new EntityTab<>(user -> new Object[]{user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getAddress(), user.getPhoneNumber()}, userColumnNames, null);
        EntityTab<ProductGetDTO> productsTab = new EntityTab<>(product -> new Object[]{product.getId(), product.getName(), product.getDescription(), product.getUnitPrice(), product.getStock(), product.getCategory()}, productColumnNames, null);
        productsTab.getTable().addMouseListener(createProductMouseListener(productsTab));
        EntityTab<OrderGetDTO> ordersTab = new EntityTab<>(order -> new Object[]{order.getId(), order.getUserId(), order.getStatus(), order.getTotal()}, orderColumnNames, null);
        ordersTab.getTable().addMouseListener(createOrderMouseListener(ordersTab));
    
        // Botón para actualizar la pestaña de usuarios
        JButton refreshUsersButton = new JButton("Actualizar");
        refreshUsersButton.addActionListener(e -> refreshUsersTab(usersTab));
        JPanel usersPanel = new JPanel(new BorderLayout());
        usersPanel.add(refreshUsersButton, BorderLayout.NORTH);
        usersPanel.add(new JScrollPane(usersTab.getTable()), BorderLayout.CENTER);
    
        // Botones para la pestaña de productos
        JButton refreshProductsButton = new JButton("Actualizar");
        refreshProductsButton.addActionListener(e -> refreshProductsTab(productsTab));
        JButton addProductButton = new JButton("Agregar Producto");
        addProductButton.addActionListener(e -> openAddProductDialog(productsTab));
        JButton deleteProductButton = new JButton("Eliminar Producto");
        deleteProductButton.addActionListener(e -> deleteSelectedProduct(productsTab));
        JButton prevPageButton = new JButton("Anterior");
        prevPageButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                refreshProductsTab(productsTab);
            }
        });
        nextPageButton = new JButton("Siguiente");
        nextPageButton.addActionListener(e -> {
            currentPage++;
            refreshProductsTab(productsTab);
        });

        // Panel para los botones de productos
        JPanel productsPanel = new JPanel(new BorderLayout());
        JPanel productsButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        productsButtonPanel.add(refreshProductsButton);
        productsButtonPanel.add(addProductButton);
        productsButtonPanel.add(deleteProductButton);
        productsButtonPanel.add(prevPageButton);
        productsButtonPanel.add(nextPageButton);
        productsPanel.add(productsButtonPanel, BorderLayout.NORTH);
        productsPanel.add(new JScrollPane(productsTab.getTable()), BorderLayout.CENTER);
    
        // Botón para actualizar la pestaña de pedidos
        JButton refreshOrdersButton = new JButton("Actualizar");
        refreshOrdersButton.addActionListener(e -> refreshOrdersTab(ordersTab));
        JPanel ordersPanel = new JPanel(new BorderLayout());
        ordersPanel.add(refreshOrdersButton, BorderLayout.NORTH);
        ordersPanel.add(new JScrollPane(ordersTab.getTable()), BorderLayout.CENTER);
    
        // Añadir las pestañas al panel de pestañas
        tabbedPane.add("Clientes", usersPanel);
        tabbedPane.add("Productos", productsPanel);
        tabbedPane.add("Pedidos", ordersPanel);
    
        // Añadir el panel de pestañas al marco principal
        frame.add(tabbedPane);
        frame.setVisible(true);
    
        // Refrescar las pestañas al iniciar la aplicación
        refreshTabs(usersTab, productsTab, ordersTab);
    }

    // Método para eliminar un producto seleccionado
    private void deleteSelectedProduct(EntityTab<ProductGetDTO> productsTab) {
        JTable table = productsTab.getTable();
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            Long productId = (Long) table.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar este producto?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    serviceContainer.productsService.delete(productId);
                    refreshProductsTab(productsTab);
                } catch (CustomError e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Ocurrió un error al eliminar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, selecciona un producto para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Método para crear un MouseAdapter para la tabla de pedidos
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

    // Método para abrir el diálogo de edición de pedidos
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

    // Método para refrescar la pestaña de pedidos
    private void refreshOrdersTab(EntityTab<OrderGetDTO> ordersTab) {
        try {
            orders = serviceContainer.ordersService.getAll();
            ordersTab.refresh(orders);
        } catch (CustomError e) {
            ordersTab.refresh(Collections.emptyList());
        }
    }

    // Método para crear un MouseAdapter para la tabla de productos
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

    // Método para abrir el diálogo de agregar producto
    private void openAddProductDialog(EntityTab<ProductGetDTO> productsTab) {
        List<Categories> categoriesList = serviceContainer.productsService.getAllCategories();
        Map<Long, String> categories = convertCategoriesToMap(categoriesList);
        ProductDialog dialog = new ProductDialog(null, categories);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            ProductCreateDTO dto = dialog.getProductCreateDTO();
            if (dto == null) {
                return; // Si dto es null, significa que hubo un error de entrada y ya se mostró un mensaje de error
            }
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
            } catch (Exception e) {
                JOptionPane.showMessageDialog(dialog, "Ocurrió un error al agregar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para abrir el diálogo de edición de producto
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

    // Método para refrescar todas las pestañas
    private void refreshTabs(EntityTab<UserGetDTO> usersTab, EntityTab<ProductGetDTO> productsTab, EntityTab<OrderGetDTO> ordersTab) {
        refreshUsersTab(usersTab);
        refreshProductsTab(productsTab);
        refreshOrdersTab(ordersTab);
    }

    // Método para refrescar la pestaña de productos
    private void refreshProductsTab(EntityTab<ProductGetDTO> productsTab) {
        try {
            products = serviceContainer.productsService.getAll(currentPage, pageSize, null, null, null, null, null, null, "id", "asc");
            productsTab.refresh(products);
            nextPageButton.setEnabled(products.size() == pageSize);
        } catch (CustomError e) {
            productsTab.refresh(Collections.emptyList());
            nextPageButton.setEnabled(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error al obtener los productos.", "Error", JOptionPane.ERROR_MESSAGE);
            nextPageButton.setEnabled(false);
        }
    }

    // Método para refrescar la pestaña de usuarios
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

    // Método para mostrar errores de validación
    private void showValidationErrors(Set<? extends ConstraintViolation<?>> violations) {
        StringBuilder errorMessage = new StringBuilder("Errores de validación:\n");
        for (ConstraintViolation<?> violation : violations) {
            errorMessage.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("\n");
        }
        JOptionPane.showMessageDialog(null, errorMessage.toString(), "Errores de validación", JOptionPane.ERROR_MESSAGE);
    }

    // Método para convertir una lista de estados a un mapa
    private Map<Long, String> convertStatusesToMap(List<Statuses> statusesList) {
        return statusesList.stream()
                .collect(Collectors.toMap(Statuses::getId, Statuses::getName));
    }

    // Método para convertir una lista de categorías a un mapa
    private Map<Long, String> convertCategoriesToMap(List<Categories> categoriesList) {
        return categoriesList.stream()
                .collect(Collectors.toMap(Categories::getId, Categories::getName));
    }
}