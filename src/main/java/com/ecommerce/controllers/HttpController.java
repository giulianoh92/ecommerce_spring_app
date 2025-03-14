package com.ecommerce.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.domain.models.Categories;
import com.ecommerce.services.ServiceContainer;
import com.ecommerce.services.Carts.dto.CartGetDTO;
import com.ecommerce.services.Carts.dto.CartItemDTO;
import com.ecommerce.services.Carts.dto.RemoveItemRequestDTO;
import com.ecommerce.services.Orders.dto.OrderGetDTO;
import com.ecommerce.services.Products.dto.CategoryCreateDTO;
import com.ecommerce.services.Products.dto.ProductCreateDTO;
import com.ecommerce.services.Products.dto.ProductGetDTO;
import com.ecommerce.services.Products.dto.ProductUpdateDTO;
import com.ecommerce.services.Users.dto.UserCreateDTO;
import com.ecommerce.services.Users.dto.UserGetDTO;
import com.ecommerce.services.Users.dto.UserLoginDTO;
import com.ecommerce.services.Users.dto.UserUpdateDTO;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Validated
@RestController
@RequestMapping("/api")
public class HttpController {

    @Autowired
    private ServiceContainer serviceContainer;

    @GetMapping("/")
    public ResponseEntity<Object> home() {
        return new ResponseEntity<>("Bienvenido a la API de E-Commerce", HttpStatus.OK);
    }
    
    // User Endpoints
    @GetMapping("/users")
    public ResponseEntity<List<UserGetDTO>> getAllUsers() {
        List<UserGetDTO> users = serviceContainer.userService.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserGetDTO> getUserById(@PathVariable long id) {
        UserGetDTO user = serviceContainer.userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserCreateDTO user) {
        serviceContainer.userService.create(user);
        return new ResponseEntity<>("Usuario registrado con éxito", HttpStatus.CREATED);
    }

    @PutMapping("/users/update/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable long id, @Valid @RequestBody UserUpdateDTO user) {
        serviceContainer.userService.update(id, user);
        return new ResponseEntity<>("Usuario actualizado con éxito", HttpStatus.OK);
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        serviceContainer.userService.delete(id);
        return new ResponseEntity<>("Usuario eliminado con éxito", HttpStatus.OK);
    }

    @PostMapping("/users/login")
    public ResponseEntity<Object> login(@Valid @RequestBody UserLoginDTO user) {
        String token = serviceContainer.userService.login(user);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    // Product Endpoints
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
        List<ProductGetDTO> products = serviceContainer.productsService.getAll(page, limit, q, categoryId, minPrice, maxPrice, inStock, active, sortBy, order);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductGetDTO> getProductById(@PathVariable long id) {
        ProductGetDTO product = serviceContainer.productsService.getById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping("/products/create")
    public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductCreateDTO product) {
        serviceContainer.productsService.create(product);
        return new ResponseEntity<>("Producto creado con éxito", HttpStatus.CREATED);
    }

    @PutMapping("/products/update/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable long id, @Valid @RequestBody ProductUpdateDTO product) {
        serviceContainer.productsService.update(id, product);
        return new ResponseEntity<>("Producto actualizado con éxito", HttpStatus.OK);
    }

    @DeleteMapping("/products/delete/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable long id) {
        serviceContainer.productsService.delete(id);
        return new ResponseEntity<>("Producto eliminado con éxito", HttpStatus.OK);
    }

    @GetMapping("/products/categories")
    public ResponseEntity<List<Categories>> getAllCategories() {
        List<Categories> categories = serviceContainer.productsService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/products/categories/{id}")
    public ResponseEntity<Categories> getCategoryById(@PathVariable long id) {
        Categories category = serviceContainer.productsService.getCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping("/products/categories")
    public ResponseEntity<Object> createCategory(@RequestBody @Valid CategoryCreateDTO category) {
        serviceContainer.productsService.createCategory(category);
        return new ResponseEntity<>("Categoría creada con éxito", HttpStatus.CREATED);
    }

    // Cart Endpoints
    @GetMapping("/carts")
    public ResponseEntity<List<CartGetDTO>> getAllCarts() {
        List<CartGetDTO> carts = serviceContainer.cartsService.getAllCarts();
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }

    @GetMapping("/carts/{id}")
    public ResponseEntity<CartGetDTO> getCartById(@PathVariable long id) {
        CartGetDTO cart = serviceContainer.cartsService.getCartById(id);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping("/carts/user/{userId}")
    public ResponseEntity<CartGetDTO> getCartByUserId(@PathVariable long userId) {
        CartGetDTO cart = serviceContainer.cartsService.getUserCart(userId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/carts/add")
    public ResponseEntity<Object> addItemToCart(@Valid @RequestBody CartItemDTO cartItem) {
        serviceContainer.cartsService.addItemToCart(cartItem.getUserId(), cartItem.getProductId(), cartItem.getQuantity());
        return new ResponseEntity<>("Producto añadido al carrito con éxito", HttpStatus.CREATED);
    }

    @DeleteMapping("/carts/remove")
    public ResponseEntity<Object> removeItemFromCart(@RequestBody @Valid RemoveItemRequestDTO request) {
        serviceContainer.cartsService.removeItemFromCart(request.getUserId(), request.getProductId());
        return new ResponseEntity<>("Producto eliminado del carrito con éxito", HttpStatus.OK);
    }

    @PutMapping("/carts/update")
    public ResponseEntity<Object> updateItemInCart(@Valid @RequestBody CartItemDTO cartItem) {
        serviceContainer.cartsService.updateItemInCart(cartItem.getUserId(), cartItem.getProductId(), cartItem.getQuantity());
        return new ResponseEntity<>("Producto actualizado en el carrito con éxito", HttpStatus.OK);
    }

    @DeleteMapping("/carts/clear/{userId}")
    public ResponseEntity<Object> clearCart(@PathVariable long userId) {
        serviceContainer.cartsService.clearCart(userId);
        return new ResponseEntity<>("Carrito vaciado con éxito", HttpStatus.OK);
    }

    @PostMapping("/carts/checkout/{userId}")
    public ResponseEntity<Object> checkout(@PathVariable long userId) {
        serviceContainer.cartsService.checkout(userId);
        return new ResponseEntity<>("Compra realizada con éxito", HttpStatus.CREATED);
    }

    // Order Endpoints
    @GetMapping("/orders")
    public ResponseEntity<List<OrderGetDTO>> getAllOrders() {
        List<OrderGetDTO> orders = serviceContainer.ordersService.getAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderGetDTO> getOrderById(@PathVariable long id) {
        OrderGetDTO order = serviceContainer.ordersService.getById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/orders/user/{userId}")
    public ResponseEntity<List<OrderGetDTO>> getOrdersByUserId(@PathVariable long userId) {
        List<OrderGetDTO> orders = serviceContainer.ordersService.getByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping("/orders/change-status/{orderId}")
    public ResponseEntity<Object> updateOrderStatus(@PathVariable long orderId, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        if (status == null) {
            return new ResponseEntity<>("Estado no proporcionado", HttpStatus.BAD_REQUEST);
        }
        serviceContainer.ordersService.updateStatus(orderId, status);
        return new ResponseEntity<>("Estado del pedido actualizado con éxito", HttpStatus.OK);
    }

    // Populate Database
    @GetMapping("/populate")
    public ResponseEntity<Object> populateDatabase() {
        serviceContainer.userService.populateDatabaseWithUsers();
        serviceContainer.productsService.populateDatabaseWithProducts();
        serviceContainer.ordersService.createStandardStatuses();
        return new ResponseEntity<>("Base de datos poblada", HttpStatus.OK);
    }
}