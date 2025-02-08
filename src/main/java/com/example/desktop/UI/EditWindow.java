package com.example.desktop.UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.List;

public class EditWindow<T> extends JDialog {

    private JTextField[] textFields;
    private JButton cancelButton;
    private JButton saveButton;
    private T item;
    private Runnable onSave;
    private DefaultTableModel tableModel;
    private int rowIndex;

    public EditWindow(Window owner, T item, Runnable onSave, DefaultTableModel tableModel, int rowIndex) {
        super(owner, "Editar Fila", ModalityType.APPLICATION_MODAL);
        this.item = item;
        this.onSave = onSave;
        this.tableModel = tableModel;
        this.rowIndex = rowIndex;

        setLayout(new BorderLayout());

        Field[] fields = item.getClass().getDeclaredFields();
        JPanel inputPanel = new JPanel(new GridLayout(fields.length, 2));
        textFields = new JTextField[fields.length];

        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            inputPanel.add(new JLabel(fields[i].getName()));
            try {
                if (List.class.isAssignableFrom(fields[i].getType())) {
                    JButton viewButton = new JButton("Ver");
                    final int index = i;
                    viewButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            showListDialog(fields[index]);
                        }
                    });
                    inputPanel.add(viewButton);
                } else {
                    textFields[i] = new JTextField(fields[i].get(item).toString());
                    if (fields[i].getName().toLowerCase().contains("id") || fields[i].getName().toLowerCase().contains("email")) {
                        textFields[i].setEditable(false);
                    }
                    inputPanel.add(textFields[i]);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        cancelButton = new JButton("CANCELAR");
        saveButton = new JButton("GUARDAR");

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    for (int i = 0; i < fields.length; i++) {
                        if (!List.class.isAssignableFrom(fields[i].getType())) {
                            String text = textFields[i].getText();
                            Object value = convertToFieldType(fields[i].getType(), text);
                            fields[i].set(item, value);
                            tableModel.setValueAt(value, rowIndex, i);
                        }
                    }
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
                dispose();
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    private Object convertToFieldType(Class<?> fieldType, String text) {
        if (fieldType == int.class || fieldType == Integer.class) {
            return Integer.parseInt(text);
        } else if (fieldType == long.class || fieldType == Long.class) {
            return Long.parseLong(text);
        } else if (fieldType == double.class || fieldType == Double.class) {
            return Double.parseDouble(text);
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return Boolean.parseBoolean(text);
        } else {
            return text;
        }
    }

    private void showListDialog(Field field) {
        try {
            List<?> list = (List<?>) field.get(item);
            if (list != null && !list.isEmpty()) {
                JDialog dialog = new JDialog(this, "Detalles de " + field.getName(), true);
                dialog.setLayout(new BorderLayout());

                Field[] listFields = list.get(0).getClass().getDeclaredFields();
                String[] columnNames = new String[listFields.length];
                for (int i = 0; i < listFields.length; i++) {
                    listFields[i].setAccessible(true);
                    columnNames[i] = listFields[i].getName();
                }

                DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
                for (Object obj : list) {
                    Object[] rowData = new Object[listFields.length];
                    for (int i = 0; i < listFields.length; i++) {
                        rowData[i] = listFields[i].get(obj);
                    }
                    tableModel.addRow(rowData);
                }

                JTable table = new JTable(tableModel);
                table.setEnabled(false); // Make the table read-only
                dialog.add(new JScrollPane(table), BorderLayout.CENTER);

                JButton closeButton = new JButton("Cerrar");
                closeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose();
                    }
                });
                dialog.add(closeButton, BorderLayout.SOUTH);

                dialog.pack();
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}