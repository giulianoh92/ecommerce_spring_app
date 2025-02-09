
package com.example.desktop.UI;

import javax.swing.table.AbstractTableModel;

import com.example.main.services.Users.dto.UserGetDTO;

import java.util.List;

public class ClientTableModel extends AbstractTableModel {

    private List<UserGetDTO> clients;
    private final String[] columnNames = {"ID", "Nombre", "Apellido", "Email", "Dirección", "Teléfono"};

    public ClientTableModel(List<UserGetDTO> clients) {
        this.clients = clients;
    }

    @Override
    public int getRowCount() {
        return clients.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        UserGetDTO client = clients.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return client.getId();
            case 1:
                return client.getFirstName();
            case 2:
                return client.getLastName();
            case 3:
                return client.getEmail();
            case 4:
                return client.getAddress();
            case 5:
                return client.getPhoneNumber();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public List<UserGetDTO> getClients() {
        return clients;
    }

    public void setClients(List<UserGetDTO> clients) {
        this.clients = clients;
    }
}