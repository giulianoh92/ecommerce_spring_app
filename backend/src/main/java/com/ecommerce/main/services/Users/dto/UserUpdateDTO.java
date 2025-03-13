package com.ecommerce.main.services.Users.dto;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.mindrot.jbcrypt.BCrypt;

public class UserUpdateDTO {

    @Size(min = 8, max = 50, message = "La contraseña debe tener entre 8 y 50 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "La contraseña debe tener al menos una letra mayúscula, una letra minúscula y un número")
    private String password;

    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String firstName;

    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String lastName;

    @Size(min = 10, max =  100, message = "La dirección debe tener entre 10 y 100 caracteres")
    private String address;

    @Pattern(regexp = "^(\\+\\d{1,3})?\\d{8,15}$", message = "El número de teléfono no es válido")
    @Size(min = 8, max = 15, message = "El número de teléfono debe tener entre 8 y 15 caracteres")
    private String phoneNumber;

    public UserUpdateDTO() {
    }

    public UserUpdateDTO(String password, String firstName, String lastName, String address, String phoneNumber) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void hashPassword() {
        this.password = BCrypt.hashpw(this.password, BCrypt.gensalt());
    }

    public boolean checkPassword(String plainPassword) {
        return BCrypt.checkpw(plainPassword, this.password);
    }
}