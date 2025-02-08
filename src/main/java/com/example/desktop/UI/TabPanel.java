package com.example.desktop.UI;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Supplier;
import com.example.main.error.CustomError;

public class TabPanel<T> extends JPanel {

    private Supplier<List<T>> dataSupplier;
    private DefaultTableModel model;
    private List<T> data;

    public TabPanel(Supplier<List<T>> dataSupplier) {
        this.dataSupplier = dataSupplier;
        this.model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells are not editable
            }
        };

        setLayout(new BorderLayout());

        JTable table = new JTable(model);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        T selectedItem = data.get(row);
                        new EditWindow<>(SwingUtilities.getWindowAncestor(TabPanel.this), selectedItem, null, model, row).setVisible(true);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Actualizar");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });
        add(refreshButton, BorderLayout.SOUTH);

        refreshTable();
    }

    private void refreshTable() {
        model.setRowCount(0); // Clear existing data
    
        try {
            data = dataSupplier.get();
            if (!data.isEmpty()) {
                T firstItem = data.get(0);
                Field[] fields = firstItem.getClass().getDeclaredFields();
                String[] columnNames = new String[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    if (!List.class.isAssignableFrom(fields[i].getType())) {
                        columnNames[i] = fields[i].getName();
                    } else {
                        columnNames[i] = null; // Mark columns with lists as null
                    }
                }
                model.setColumnIdentifiers(columnNames);
    
                for (T item : data) {
                    Object[] rowData = new Object[fields.length];
                    for (int i = 0; i < fields.length; i++) {
                        if (columnNames[i] != null) {
                            rowData[i] = fields[i].get(item);
                        } else {
                            rowData[i] = "Ver detalles";
                        }
                    }
                    model.addRow(rowData);
                }
            }
        } catch (CustomError e) {
            if (e.getCode() != 4004) { // If the error is not a 404, show the error message
                JOptionPane.showMessageDialog(this, "Error al obtener datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            // If the error is a 404, do nothing and leave the table empty
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al obtener datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}