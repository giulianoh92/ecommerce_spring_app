package com.example.web.controller;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

import com.example.main.controllers.MainController;
import com.example.main.domain.models.Categories;
import com.example.main.services.Carts.dto.CartGetDTO;
import com.example.main.services.Carts.dto.CartItemDTO;
import com.example.main.services.Carts.dto.RemoveItemRequestDTO;
import com.example.main.services.Orders.dto.OrderGetDTO;
import com.example.main.services.Products.dto.CategoryCreateDTO;
import com.example.main.services.Products.dto.ProductCreateDTO;
import com.example.main.services.Products.dto.ProductGetDTO;
import com.example.main.services.Products.dto.ProductUpdateDTO;
import com.example.main.services.Users.dto.UserCreateDTO;
import com.example.main.services.Users.dto.UserGetDTO;
import com.example.main.services.Users.dto.UserLoginDTO;
import com.example.main.services.Users.dto.UserUpdateDTO;


@Validated
@RestController
public class HttpController {

    @Autowired
    private MainController mainController;

    @GetMapping("/users")
    public ResponseEntity<List<UserGetDTO>> getAllUsers() {
        List<UserGetDTO> users = mainController.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserGetDTO> getUserById(@PathVariable long id) {
        UserGetDTO user = mainController.getUserById(id);
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
        String token = mainController.login(user);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductGetDTO>> getAllProducts(
        @RequestParam int page,
        @RequestParam int limit,
        @RequestParam(required = false) String q,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(required = false) Boolean inStock,
        @RequestParam(required = false) Boolean active,
        @RequestParam(required = false, defaultValue = "id") String sortBy,
        @RequestParam(required = false, defaultValue = "asc") String order
    ) {
        List<ProductGetDTO> products = mainController.getAllProducts(page, limit, q, categoryId, minPrice, maxPrice, inStock, active, sortBy, order);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductGetDTO> getProductById(@PathVariable long id) {
        ProductGetDTO product = mainController.getProductById(id);
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

    @GetMapping("/users/cart")
    public ResponseEntity<Object> getAllCarts() {
        List<CartGetDTO> carts = mainController.getAllCarts();
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }

    @GetMapping("/users/cart/{id}")
    public ResponseEntity<Object> getCartById(@PathVariable long id) {
        CartGetDTO cart = mainController.getCartById(id);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping("/users/cart/user/{userId}")
    public ResponseEntity<Object> getCartByUserId(@PathVariable long userId) {
        CartGetDTO cart = mainController.getCartByUserId(userId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
    
    @PostMapping("/users/cart/add")
    public ResponseEntity<Object> addItemToCart(@Valid @RequestBody CartItemDTO cartItem) {
        mainController.addItemToCart(cartItem.getUserId(), cartItem.getProductId(), cartItem.getQuantity());
        return new ResponseEntity<>("Producto añadido al carrito con éxito", HttpStatus.CREATED);
    }

    @DeleteMapping("/users/cart/remove")
    public ResponseEntity<Object> removeItemFromCart(@RequestBody @Valid RemoveItemRequestDTO request) {
        mainController.removeItemFromCart(request.getUserId(), request.getProductId());
        return new ResponseEntity<>("Producto eliminado del carrito con éxito", HttpStatus.OK);
    }

    @PutMapping("/users/cart/update")
    public ResponseEntity<Object> updateItemInCart(@Valid @RequestBody CartItemDTO cartItem) {
        mainController.updateItemInCart(cartItem.getUserId(), cartItem.getProductId(), cartItem.getQuantity());
        return new ResponseEntity<>("Producto actualizado en el carrito con éxito", HttpStatus.OK);
    }

    @DeleteMapping("/users/cart/clear/{userId}")
    public ResponseEntity<Object> clearCart(@PathVariable long userId) {
        mainController.clearCart(userId);
        return new ResponseEntity<>("Carrito vaciado con éxito", HttpStatus.OK);
    }

    @PostMapping("/users/cart/checkout/{userId}")
    public ResponseEntity<Object> checkout(@PathVariable long userId) {
        mainController.checkout(userId);
        return new ResponseEntity<>("Compra realizada con éxito", HttpStatus.CREATED);
    }

    @GetMapping("/users/orders")
    public ResponseEntity<Object> getAllOrders() {
        List<OrderGetDTO> orders = mainController.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/users/orders/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable long id) {
        OrderGetDTO order = mainController.getOrderById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/users/orders/user/{userId}")
    public ResponseEntity<Object> getOrdersByUserId(@PathVariable long userId) {
        List<OrderGetDTO> orders = mainController.getOrdersByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping("/users/orders/change-status/{orderId}")
    public ResponseEntity<Object> updateOrderStatus(@PathVariable long orderId, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        if (status == null) {
            return new ResponseEntity<>("Estado no proporcionado", HttpStatus.BAD_REQUEST);
        }
        mainController.updateOrderStatus(orderId, status);
        return new ResponseEntity<>("Estado del pedido actualizado con éxito", HttpStatus.OK);
    }
    
    @GetMapping("/populate")
    public ResponseEntity<Object> populateDatabase() {
        mainController.populateDatabaseWithSampleData();
        return new ResponseEntity<>("Base de datos poblada", HttpStatus.OK);
    }

}
