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
import jakarta.validation.constraints.NotNull;

import com.example.main.controllers.MainController;
import com.example.main.domain.models.Categories;
import com.example.main.domain.models.Products;
import com.example.main.domain.models.Users;
import com.example.main.services.Products.dto.CategoryCreateDTO;
import com.example.main.services.Products.dto.ProductCreateDTO;
import com.example.main.services.Products.dto.ProductUpdateDTO;
import com.example.main.services.Users.dto.UserCreateDTO;
import com.example.main.services.Users.dto.UserLoginDTO;
import com.example.main.services.Users.dto.UserUpdateDTO;
import org.springframework.web.bind.annotation.RequestParam;


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
    public ResponseEntity<Object> login(@Valid @RequestBody UserLoginDTO user) {
        mainController.login(user);
        return new ResponseEntity<>("Usuario logueado con éxito", HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Products>> getAllProducts() {
        List<Products> products = mainController.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Products> getProductById(@PathVariable long id) {
        Products product = mainController.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping("/products/create")
    public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductCreateDTO product) {
        mainController.createProduct(product);
        return new ResponseEntity<>("Producto creado con éxito", HttpStatus.CREATED);
    }

    @PutMapping("/products/update/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable long id, @Valid @RequestBody ProductUpdateDTO product) {
        mainController.updateProduct(id, product);
        return new ResponseEntity<>("Producto actualizado con éxito", HttpStatus.OK);
    }

    @DeleteMapping("/products/delete/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable long id) {
        mainController.deleteProduct(id);
        return new ResponseEntity<>("Producto eliminado con éxito", HttpStatus.OK);
    }

    @GetMapping("/products/categories")
    public ResponseEntity<List<Categories>> getAllCategories() {
        List<Categories> categories = mainController.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/products/categories/{id}")
    public ResponseEntity<Categories> getCategoryById(@PathVariable long id) {
        Categories category = mainController.getCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping("/products/categories")
    public ResponseEntity<Object> createCategory(@RequestBody @Valid CategoryCreateDTO category) {
        mainController.createCategory(category);
        return new ResponseEntity<>("Categoría creada con éxito", HttpStatus.CREATED);
    }
    
    

}
