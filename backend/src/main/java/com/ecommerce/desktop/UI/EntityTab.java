package com.ecommerce.desktop.UI;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class EntityTab<T> extends JPanel {
    private JTable table;
    private List<T> data;
    @SuppressWarnings("unused")
    private Function<T, Object[]> rowMapper;
    @SuppressWarnings("unused")
    private String[] columnNames;

    // Constructor para inicializar la pestaña de entidad
    public EntityTab(Function<T, Object[]> rowMapper, String[] columnNames, MouseListener mouseListener) {
        this.rowMapper = rowMapper;
        this.columnNames = columnNames;
        setLayout(new BorderLayout());
        table = new JTable(new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return data == null ? 0 : data.size();
            }

            @Override
            public int getColumnCount() {
                return columnNames.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                T item = data.get(rowIndex);
                return rowMapper.apply(item)[columnIndex];
            }

            @Override
            public String getColumnName(int column) {
                return columnNames[column];
            }
        });

        if (mouseListener != null) {
            table.addMouseListener(mouseListener);
        }

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    // Método para refrescar los datos de la tabla
    public void refresh(List<T> data) {
        this.data = data != null ? data : Collections.emptyList();
        ((AbstractTableModel) table.getModel()).fireTableDataChanged();
    }

    // Método para obtener la tabla
    public JTable getTable() {
        return table;
    }
}