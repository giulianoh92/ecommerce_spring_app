package com.example.desktop.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditWindow extends JDialog {

    private JTextField[] textFields;
    private JButton cancelButton;
    private JButton saveButton;
    private Object[] rowData;
    private Runnable onSave;

    public EditWindow(Window owner, String[] columnNames, Object[] rowData, Runnable onSave) {
        super(owner, "Editar Fila", ModalityType.APPLICATION_MODAL);
        this.rowData = rowData;
        this.onSave = onSave;

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(columnNames.length, 2));
        textFields = new JTextField[columnNames.length];

        for (int i = 0; i < columnNames.length; i++) {
            inputPanel.add(new JLabel(columnNames[i]));
            textFields[i] = new JTextField(rowData[i].toString());
            if (columnNames[i].toLowerCase().contains("id") || columnNames[i].toLowerCase().contains("email")) {
                textFields[i].setEditable(false);
            }
            inputPanel.add(textFields[i]);
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
                for (int i = 0; i < textFields.length; i++) {
                    rowData[i] = textFields[i].getText();
                }
                onSave.run();
                dispose();
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }
}