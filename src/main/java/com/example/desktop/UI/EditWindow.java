package com.example.desktop.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

public class EditWindow<T> extends JDialog {

    private JTextField[] textFields;
    private JButton cancelButton;
    private JButton saveButton;
    private T item;
    private Runnable onSave;

    public EditWindow(Window owner, T item, Runnable onSave) {
        super(owner, "Editar Fila", ModalityType.APPLICATION_MODAL);
        this.item = item;
        this.onSave = onSave;

        setLayout(new BorderLayout());

        Field[] fields = item.getClass().getDeclaredFields();
        JPanel inputPanel = new JPanel(new GridLayout(fields.length, 2));
        textFields = new JTextField[fields.length];

        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            inputPanel.add(new JLabel(fields[i].getName()));
            try {
                textFields[i] = new JTextField(fields[i].get(item).toString());
                if (fields[i].getName().toLowerCase().contains("id") || fields[i].getName().toLowerCase().contains("email")) {
                    textFields[i].setEditable(false);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
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
                try {
                    for (int i = 0; i < fields.length; i++) {
                        fields[i].set(item, textFields[i].getText());
                    }
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
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