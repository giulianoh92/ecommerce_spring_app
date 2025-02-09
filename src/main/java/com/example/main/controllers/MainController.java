package com.example.main.controllers;

import com.example.main.domain.models.Categories;
import com.example.main.services.ServiceContainer;
import com.example.main.services.Carts.dto.CartGetDTO;
import com.example.main.services.Orders.dto.OrderGetDTO;
import com.example.main.services.Products.dto.CategoryCreateDTO;
import com.example.main.services.Products.dto.ProductCreateDTO;
import com.example.main.services.Products.dto.ProductGetDTO;
import com.example.main.services.Products.dto.ProductUpdateDTO;
import com.example.main.services.Users.dto.UserCreateDTO;
import com.example.main.services.Users.dto.UserGetDTO;
import com.example.main.services.Users.dto.UserLoginDTO;
import com.example.main.services.Users.dto.UserUpdateDTO;

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

    public List<UserGetDTO> getAllUsers() {
        return serviceContainer.userService.getAll();
    }

    public UserGetDTO getUserById(long id) {
        return serviceContainer.userService.getById(id);
    }

    public void registerUser(@Valid UserCreateDTO user) {
        serviceContainer.userService.create(user);
    }

    public void updateUser(long id, @Valid UserUpdateDTO user) {
        serviceContainer.userService.update(id, user);
    }

    public void deleteUser(long id) {
        serviceContainer.userService.delete(id);
    }

    public void login(UserLoginDTO user) {
        serviceContainer.userService.login(user);
    }

    public List<ProductGetDTO> getAllProducts() {
        return serviceContainer.productsService.getAll();
    }

    public ProductGetDTO getProductById(long id) {
        return serviceContainer.productsService.getById(id);
    }

    public void createProduct(ProductCreateDTO product) {
        serviceContainer.productsService.create(product);
    }

    public void updateProduct(long id, ProductUpdateDTO product) {
        serviceContainer.productsService.update(id, product);
    }

    public void deleteProduct(long id) {
        serviceContainer.productsService.delete(id);
    }

    public List<Categories> getAllCategories() {
        List<Categories> categories = serviceContainer.productsService.getAllCategories();
        return categories;
    }

    public Categories getCategoryById(long id) {
        Categories category = serviceContainer.productsService.getCategoryById(id);
        return category;
    }

    public void createCategory(CategoryCreateDTO category) {
        serviceContainer.productsService.createCategory(category);
    }

    public void addItemToCart(long userId, long productId, int quantity) {
        serviceContainer.cartsService.addItemToCart(userId, productId, quantity);
    }

    public void removeItemFromCart(long userId, long productId) {
        serviceContainer.cartsService.removeItemFromCart(userId, productId);
    }

    public void updateItemInCart(long userId, long productId, int quantity) {
        serviceContainer.cartsService.updateItemInCart(userId, productId, quantity);
    }

    public void clearCart(long userId) {
        serviceContainer.cartsService.clearCart(userId);
    }

    public void checkout(long userId) {
        serviceContainer.cartsService.checkout(userId);
    }

    public List<CartGetDTO> getAllCarts() {
        return serviceContainer.cartsService.getAllCarts();
    }

    public CartGetDTO getCartById(long id) {
        return serviceContainer.cartsService.getCartById(id);
    }

    public CartGetDTO getCartByUserId(long userId) {
        return serviceContainer.cartsService.getUserCart(userId);
    }

    public void updateOrderStatus(long orderId, String status) {
        serviceContainer.ordersService.updateStatus(orderId, status);
    }

    public List<OrderGetDTO> getAllOrders() {
        return serviceContainer.ordersService.getAll();
    }

    public OrderGetDTO getOrderById(long id) {
        return serviceContainer.ordersService.getById(id);
    }

    public List<OrderGetDTO> getOrdersByUserId(long userId) {
        return serviceContainer.ordersService.getByUserId(userId);
    }

    public void populateDatabaseWithSampleData() {
        serviceContainer.productsService.populateDatabaseWithProducts();
    }

    public void populateDatabaseWithStatuses() {
        serviceContainer.ordersService.createStandardStatuses();
    }
}