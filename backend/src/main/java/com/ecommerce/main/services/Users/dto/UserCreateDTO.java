package com.ecommerce.main.services.Users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.mindrot.jbcrypt.BCrypt;

public class UserCreateDTO {

    @NotNull(message = "El email es requerido")
    @NotBlank(message = "El email es requerido")
    @Email(message = "El email no es válido")
    @Size(min = 5, max = 50, message = "El email debe tener entre 5 y 50 caracteres")
    private String email;

    @NotNull(message = "La contraseña es requerida")
    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 8, max = 50, message = "La contraseña debe tener entre 8 y 50 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "La contraseña debe tener al menos una letra mayúscula, una letra minúscula y un número")
    private String password;

    @NotNull(message = "El nombre es requerido")
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String firstName;

    @NotNull(message = "El apellido es requerido")
    @NotBlank(message = "El apellido es requerido")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String lastName;

    @NotNull(message = "La dirección es requerida")
    @NotBlank(message = "La dirección es requerida")
    @Size(min = 10, max =  100, message = "La dirección debe tener entre 10 y 100 caracteres")
    private String address;

    @NotNull(message = "El número de teléfono es requerido")
    @NotBlank(message = "El número de teléfono es requerido")
    @Pattern(regexp = "^(\\+\\d{1,3})?\\d{8,15}$", message = "El número de teléfono no es válido")
    @Size(min = 8, max = 15, message = "El número de teléfono debe tener entre 8 y 15 caracteres")
    private String phoneNumber;

    public UserCreateDTO() {
    }

    public UserCreateDTO(String email, String password, String firstName, String lastName, String address, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public String toString() {
        return "UserCreateDTO [address=" + address + ", email=" + email + ", firstName=" + firstName + ", lastName="
                + lastName + ", password=" + password + ", phoneNumber=" + phoneNumber + "]";
    }
}