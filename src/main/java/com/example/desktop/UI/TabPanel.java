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
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.Consumer;
import com.example.main.error.CustomError;

public class TabPanel<T> extends JPanel {

    private Supplier<List<T>> dataSupplier;
    private String[] columnNames;
    private Function<T, Object[]> rowMapper;
    private DefaultTableModel model;

    public TabPanel(Supplier<List<T>> dataSupplier, String[] columnNames, Function<T, Object[]> rowMapper) {
        this.dataSupplier = dataSupplier;
        this.columnNames = columnNames;
        this.rowMapper = rowMapper;
        this.model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells are not editable
            }
        };

        setLayout(new BorderLayout());

        JTable table = new JTable(model);

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
            List<T> data = dataSupplier.get();
            for (T item : data) {
                model.addRow(rowMapper.apply(item));
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