package com.example.desktop.UI;

import javax.swing.table.AbstractTableModel;

import com.example.main.services.Products.dto.ProductGetDTO;

import java.util.List;

public class ProductTableModel extends AbstractTableModel {

    private List<ProductGetDTO> products;
    private final String[] columnNames = {"ID", "Name", "Price"};

    public ProductTableModel(List<ProductGetDTO> products) {
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
        ProductGetDTO product = products.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return product.getId();
            case 1:
                return product.getName();
            case 2:
                return product.getUnitPrice();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public List<ProductGetDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductGetDTO> products) {
        this.products = products;
    }
}