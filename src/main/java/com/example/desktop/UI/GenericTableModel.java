package com.example.desktop.UI;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public abstract class GenericTableModel<T> extends AbstractTableModel {

    private List<T> data;
    private final String[] columnNames;

    public GenericTableModel(String[] columnNames) {
        this.columnNames = columnNames;
    }

    @Override
    public int getRowCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setData(List<T> data) {
        this.data = data;
        fireTableDataChanged();
    }

    public T getDataAt(int rowIndex) {
        return data.get(rowIndex);
    }
}