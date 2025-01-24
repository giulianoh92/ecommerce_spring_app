package com.example.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

import com.example.main.controllers.MainController;
import com.example.main.domain.models.Users;
import com.example.main.services.Users.dto.UserCreateDTO;
import com.example.main.services.Users.dto.UserUpdateDTO;

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

    @PostMapping("/users/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserCreateDTO user) {
        mainController.registerUser(user);
        return new ResponseEntity<>("Usuario registrado con éxito", HttpStatus.CREATED);
    }

    @PutMapping("/users/update/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable long id, @Valid @RequestBody UserUpdateDTO user) {
        mainController.updateUser(id, user);
        return new ResponseEntity<>("Usuario actualizado con éxito", HttpStatus.OK);
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        mainController.deleteUser(id);
        return new ResponseEntity<>("Usuario eliminado con éxito", HttpStatus.OK);
    }

    @PostMapping("/users/login")
    public ResponseEntity<Object> login(@RequestBody UserCreateDTO user) {
        mainController.login(user.getEmail(), user.getPassword());
        return new ResponseEntity<>("Usuario logueado con éxito", HttpStatus.OK);
    }


}
