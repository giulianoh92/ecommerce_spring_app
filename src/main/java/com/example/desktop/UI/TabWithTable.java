package com.example.desktop.UI;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class TabWithTable<T> extends JPanel {

    private JTable table;
    private AbstractTableModel tableModel;

    public TabWithTable(AbstractTableModel tableModel) {
        this.tableModel = tableModel;
        this.table = new JTable(tableModel);
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    @SuppressWarnings("unchecked")
    public void setData(List<T> data) {
        if (tableModel instanceof GenericTableModel) {
            ((GenericTableModel<T>) tableModel).setData(data);
        }
    }
}