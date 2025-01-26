package com.example.main.controllers;

import com.example.main.domain.models.Categories;
import com.example.main.domain.models.Products;
import com.example.main.domain.models.Users;
import com.example.main.services.ServiceContainer;
import com.example.main.services.Products.dto.CategoryCreateDTO;
import com.example.main.services.Products.dto.ProductCreateDTO;
import com.example.main.services.Products.dto.ProductUpdateDTO;
import com.example.main.services.Users.dto.UserCreateDTO;
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

    public List<Users> getAllUsers() {
        List<Users> users = serviceContainer.userService.getAll();
        return users;
    }

    public Users getUserById(long id) {
        Users user = serviceContainer.userService.getById(id);
        return user;
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
        serviceContainer.userService.login(user.getEmail(), user.getPassword());
    }

    public List<Products> getAllProducts() {
        List<Products> products = serviceContainer.productsService.getAll();
        return products;
    }

    public Products getProductById(long id) {
        Products product = serviceContainer.productsService.getById(id);
        return product;
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
}