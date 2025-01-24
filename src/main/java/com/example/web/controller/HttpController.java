package com.example.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

import com.example.main.controllers.MainController;
import com.example.main.domain.models.Users;
import com.example.main.services.Users.dto.UserCreateDTO;

@Validated
@RestController
public class HttpController {

    @Autowired
    private MainController mainController;

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = mainController.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable long id) {
        Users user = mainController.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Usamos @Valid para que Spring valide el DTO antes de pasarlo al controlador
    @PostMapping("/users/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserCreateDTO user) {
        System.out.println("UserCreateDTO supera la validación en controlador HTTP: " + user.toString());
        System.out.println("Se procede a enviar el DTO al controlador principal");
        mainController.registerUser(user);
        return new ResponseEntity<>("Usuario registrado con éxito", HttpStatus.CREATED);
    }


}
