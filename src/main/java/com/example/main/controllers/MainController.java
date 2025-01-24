package com.example.main.controllers;

import com.example.main.domain.models.Users;
import com.example.main.services.ServiceContainer;
import com.example.main.services.Users.dto.UserCreateDTO;

import jakarta.validation.Valid;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Validated
@Controller
public class MainController {
    @Autowired
    private ServiceContainer serviceContainer;

    public List<Users> getAllUsers() {
        List<Users> users = serviceContainer.userService.getAll();
        return users;
    }

    public Users getUserById(long id) {
        Users user = serviceContainer.userService.getById(id);
        return user;
    }

    public void registerUser(@Valid UserCreateDTO user) {
        System.out.println("UserCreateDTO llega al controlador principal: " + user.toString());
        serviceContainer.userService.create(user);
    }
}