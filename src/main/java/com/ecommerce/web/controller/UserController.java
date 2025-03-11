package com.ecommerce.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.main.controllers.MainController;
import com.ecommerce.main.services.Carts.dto.CartGetDTO;
import com.ecommerce.main.services.Carts.dto.CartItemDTO;
import com.ecommerce.main.services.Carts.dto.RemoveItemRequestDTO;
import com.ecommerce.main.services.Orders.dto.OrderGetDTO;
import com.ecommerce.main.services.Users.dto.UserCreateDTO;
import com.ecommerce.main.services.Users.dto.UserGetDTO;
import com.ecommerce.main.services.Users.dto.UserLoginDTO;
import com.ecommerce.main.services.Users.dto.UserUpdateDTO;

import jakarta.validation.Valid;

/*
 * Clase controladora para la creación de endpoints HTTP
 * permite la creación de endpoints para la comunicación con el frontend
 * 
 */
@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private MainController mainController;

    @GetMapping
    public ResponseEntity<List<UserGetDTO>> getAllUsers() {
        List<UserGetDTO> users = mainController.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetDTO> getUserById(@PathVariable long id) {
        UserGetDTO user = mainController.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserCreateDTO user) {
        mainController.registerUser(user);
        return new ResponseEntity<>("Usuario registrado con éxito", HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable long id, @Valid @RequestBody UserUpdateDTO user) {
        mainController.updateUser(id, user);
        return new ResponseEntity<>("Usuario actualizado con éxito", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        mainController.deleteUser(id);
        return new ResponseEntity<>("Usuario eliminado con éxito", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody UserLoginDTO user) {
        String token = mainController.login(user);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/cart")
    public ResponseEntity<Object> getAllCarts() {
        List<CartGetDTO> carts = mainController.getAllCarts();
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }

    @GetMapping("/cart/{id}")
    public ResponseEntity<Object> getCartById(@PathVariable long id) {
        CartGetDTO cart = mainController.getCartById(id);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping("/cart/user/{userId}")
    public ResponseEntity<Object> getCartByUserId(@PathVariable long userId) {
        CartGetDTO cart = mainController.getCartByUserId(userId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
    
    @PostMapping("/cart/add")
    public ResponseEntity<Object> addItemToCart(@Valid @RequestBody CartItemDTO cartItem) {
        mainController.addItemToCart(cartItem.getUserId(), cartItem.getProductId(), cartItem.getQuantity());
        return new ResponseEntity<>("Producto añadido al carrito con éxito", HttpStatus.CREATED);
    }

    @DeleteMapping("/cart/remove")
    public ResponseEntity<Object> removeItemFromCart(@RequestBody @Valid RemoveItemRequestDTO request) {
        mainController.removeItemFromCart(request.getUserId(), request.getProductId());
        return new ResponseEntity<>("Producto eliminado del carrito con éxito", HttpStatus.OK);
    }

    @PutMapping("/cart/update")
    public ResponseEntity<Object> updateItemInCart(@Valid @RequestBody CartItemDTO cartItem) {
        mainController.updateItemInCart(cartItem.getUserId(), cartItem.getProductId(), cartItem.getQuantity());
        return new ResponseEntity<>("Producto actualizado en el carrito con éxito", HttpStatus.OK);
    }

    @DeleteMapping("/cart/clear/{userId}")
    public ResponseEntity<Object> clearCart(@PathVariable long userId) {
        mainController.clearCart(userId);
        return new ResponseEntity<>("Carrito vaciado con éxito", HttpStatus.OK);
    }

    @PostMapping("/cart/checkout/{userId}")
    public ResponseEntity<Object> checkout(@PathVariable long userId) {
        mainController.checkout(userId);
        return new ResponseEntity<>("Compra realizada con éxito", HttpStatus.CREATED);
    }

    @GetMapping("/orders")
    public ResponseEntity<Object> getAllOrders() {
        List<OrderGetDTO> orders = mainController.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable long id) {
        OrderGetDTO order = mainController.getOrderById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/orders/user/{userId}")
    public ResponseEntity<Object> getOrdersByUserId(@PathVariable long userId) {
        List<OrderGetDTO> orders = mainController.getOrdersByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping("/orders/change-status/{orderId}")
    public ResponseEntity<Object> updateOrderStatus(@PathVariable long orderId, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        if (status == null) {
            return new ResponseEntity<>("Estado no proporcionado", HttpStatus.BAD_REQUEST);
        }
        mainController.updateOrderStatus(orderId, status);
        return new ResponseEntity<>("Estado del pedido actualizado con éxito", HttpStatus.OK);
    }
}