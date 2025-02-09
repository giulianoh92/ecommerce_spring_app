package com.example.desktop.UI;

import com.example.main.services.Users.dto.UserGetDTO;

public class ClientTableModel extends GenericTableModel<UserGetDTO> {

    private static final String[] COLUMN_NAMES = {"ID", "Nombre", "Apellido", "Email", "Dirección", "Teléfono"};

    public ClientTableModel() {
        super(COLUMN_NAMES);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        UserGetDTO client = getDataAt(rowIndex);
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
}