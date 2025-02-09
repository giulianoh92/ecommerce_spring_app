package com.example.desktop.UI;

import com.example.main.services.Products.dto.ProductGetDTO;

public class ProductTableModel extends GenericTableModel<ProductGetDTO> {

    private static final String[] COLUMN_NAMES = {"ID", "Name", "Price"};

    public ProductTableModel() {
        super(COLUMN_NAMES);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ProductGetDTO product = getDataAt(rowIndex);
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
}