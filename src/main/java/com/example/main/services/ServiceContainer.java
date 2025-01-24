package com.example.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.main.services.Users.UserService;

import com.example.main.domain.repositories.OrdersRepository;
import com.example.main.domain.repositories.ProductsRepository;
import com.example.main.domain.repositories.UsersRepository;

@Component
public class ServiceContainer {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    public UserService userService = new UserService(usersRepository);

}