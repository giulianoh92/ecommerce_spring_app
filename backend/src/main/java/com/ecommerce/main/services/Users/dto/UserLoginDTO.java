package com.ecommerce.main.services.Users.dto;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserLoginDTO {

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

    public UserLoginDTO() {
    }

    public UserLoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
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

    public boolean checkPassword(String plainPassword) {
        return BCrypt.checkpw(plainPassword, this.password);
    }
}