package com.example.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.main.services.Carts.CartsService;
import com.example.main.services.Orders.OrdersService;
import com.example.main.services.Products.ProductsService;
import com.example.main.services.Users.UserService;
import com.example.main.domain.repositories.CartsRepository;
import com.example.main.domain.repositories.CategoriesRepository;
import com.example.main.domain.repositories.ItemsRepository;
import com.example.main.domain.repositories.OrdersRepository;
import com.example.main.domain.repositories.ProductsRepository;
import com.example.main.domain.repositories.StatusesRepository;
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
    private CartsRepository cartsRepository;

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired 
    private StatusesRepository statusesRepository;

    @Autowired
    public UserService userService = new UserService(usersRepository, cartsRepository);

    @Autowired
    public OrdersService ordersService = new OrdersService(ordersRepository, itemsRepository, statusesRepository);

    @Autowired
    public ProductsService productsService = new ProductsService(productsRepository, categoriesRepository);

    @Autowired
    public CartsService cartsService = new CartsService(cartsRepository, productsRepository, usersRepository, ordersRepository, itemsRepository, statusesRepository);

}