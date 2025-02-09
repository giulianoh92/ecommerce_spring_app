package com.example.desktop.UI;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.example.main.services.Orders.dto.OrderGetDTO;

public class OrderTableModel extends AbstractTableModel  {

    private List<OrderGetDTO> products;
    private final String[] columnNames = {"ID", "ID del cliente", "Estado", "Total"};

    public OrderTableModel(List<OrderGetDTO> products) {
        this.products = products;
    }

    @Override
    public int getRowCount() {
        return products.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        OrderGetDTO product = products.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return product.getId();
            case 1:
                return product.getUserId();
            case 2:
                return product.getStatus();
            case 3:
                return product.getTotal();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public List<OrderGetDTO> getProducts() {
        return products;
    }

    public void setProducts(List<OrderGetDTO> products) {
        this.products = products;
    }
}
