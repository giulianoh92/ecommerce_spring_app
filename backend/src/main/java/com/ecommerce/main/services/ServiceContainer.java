package com.ecommerce.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ecommerce.main.domain.repositories.CartsRepository;
import com.ecommerce.main.domain.repositories.CategoriesRepository;
import com.ecommerce.main.domain.repositories.ItemsRepository;
import com.ecommerce.main.domain.repositories.OrdersRepository;
import com.ecommerce.main.domain.repositories.ProductsRepository;
import com.ecommerce.main.domain.repositories.StatusesRepository;
import com.ecommerce.main.domain.repositories.UsersRepository;
import com.ecommerce.main.services.Carts.CartsService;
import com.ecommerce.main.services.Orders.OrdersService;
import com.ecommerce.main.services.Products.ProductsService;
import com.ecommerce.main.services.Users.UserService;

/*
 * ServiceContainer
 * esta clase se encarga de contener todos los servicios de la aplicación
 * y de inyectar las dependencias necesarias para cada uno de ellos
 */
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
    public OrdersService ordersService = new OrdersService(ordersRepository, statusesRepository);

    @Autowired
    public ProductsService productsService = new ProductsService(productsRepository, categoriesRepository);

    @Autowired
    public CartsService cartsService = new CartsService(cartsRepository, productsRepository, usersRepository, ordersRepository, itemsRepository, statusesRepository);

}