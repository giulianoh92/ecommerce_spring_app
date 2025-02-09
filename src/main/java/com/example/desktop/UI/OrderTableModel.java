package com.example.desktop.UI;

import com.example.main.services.Orders.dto.OrderGetDTO;

public class OrderTableModel extends GenericTableModel<OrderGetDTO> {

    private static final String[] COLUMN_NAMES = {"ID", "ID del cliente", "Estado", "Total"};

    public OrderTableModel() {
        super(COLUMN_NAMES);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        OrderGetDTO order = getDataAt(rowIndex);
        switch (columnIndex) {
            case 0:
                return order.getId();
            case 1:
                return order.getUserId();
            case 2:
                return order.getStatus();
            case 3:
                return order.getTotal();
            default:
                return null;
        }
    }
}