package com.example.main.services.Carts;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.main.domain.models.Carts;
import com.example.main.domain.models.Items;
import com.example.main.domain.models.Orders;
import com.example.main.domain.models.Users;
import com.example.main.domain.repositories.CartsRepository;
import com.example.main.domain.repositories.ItemsRepository;
import com.example.main.domain.repositories.OrdersRepository;
import com.example.main.domain.repositories.ProductsRepository;
import com.example.main.domain.repositories.UsersRepository;
import com.example.main.error.CustomError;
import com.example.main.services.Carts.dto.CartGetDTO;

@Service
public class CartsService {

    @Autowired
    private final CartsRepository cartsRepository;

    @Autowired
    private final ProductsRepository productsRepository;
 
    @Autowired
    private final UsersRepository usersRepository;

    @Autowired 
    private final OrdersRepository ordersRepository;

    @Autowired
    private final ItemsRepository itemsRepository;

    public CartsService(
        CartsRepository cartsRepository,
        ProductsRepository productsRepository,
        UsersRepository usersRepository,
        OrdersRepository ordersRepository,
        ItemsRepository itemsRepository
    ) {
        this.cartsRepository = cartsRepository;
        this.productsRepository = productsRepository;
        this.usersRepository = usersRepository;
        this.ordersRepository = ordersRepository;
        this.itemsRepository = itemsRepository;
    }

    public void createCart(long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(
            () -> new CustomError(4004, "Usuario no encontrado")
        );
        Carts cart = new Carts(user);
        cartsRepository.save(cart);
    }

    public void addItemToCart(long userId, long productId, int quantity) {
        Carts cart = cartsRepository.findByUserId(userId).orElseThrow(
            () -> new CustomError(4004, "Carrito no encontrado")
        );
        Items item = new Items(productsRepository.findById(productId).orElseThrow(
            () -> new CustomError(4004, "Producto no encontrado")
        ), quantity);
        item.setCart(cart); // Asocia el item con el carrito
        itemsRepository.save(item); // Guarda el item en la base de datos
        cart.addItem(item);
        cartsRepository.save(cart);
    }

    public void removeItemFromCart(long userId, long productId) {
        Carts cart = cartsRepository.findById(userId).orElseThrow(
            () -> new CustomError(4004, "Carrito no encontrado")
        );
        Items item = cart.getItems().stream().filter(i -> i.getProduct().getId() == productId).findFirst().orElseThrow(
            () -> new CustomError(4004, "Producto no encontrado")
        );
        cart.removeItem(item);
        itemsRepository.delete(item);
        cartsRepository.save(cart);
    }

    public void updateItemInCart(long userId, long productId, int quantity) {
        Carts cart = cartsRepository.findById(userId).orElseThrow(
            () -> new CustomError(4004, "Carrito no encontrado")
        );
        Items item = cart.getItems().stream().filter(i -> i.getProduct().getId() == productId).findFirst().orElseThrow(
            () -> new CustomError(4004, "Producto no encontrado")
        );
        item.setQuantity(quantity);
        itemsRepository.save(item);
        cart.updateItem(item, quantity);
        cartsRepository.save(cart);
    }

    public void clearCart(long userId) {
        Carts cart = cartsRepository.findById(userId).orElseThrow(
            () -> new CustomError(4004, "Carrito no encontrado")
        );
        List<Items> items = cart.getItems();
        itemsRepository.deleteAll(items);
        cart.setItems(null);
        cartsRepository.save(cart);
    }

    public void checkout(long userId) {
        Carts cart = cartsRepository.findById(userId).orElseThrow(
            () -> new CustomError(4004, "Carrito no encontrado")
        );
        Orders order = ordersRepository.save(new Orders(cart));
        itemsRepository.saveAll(order.getItems().stream().peek(item -> item.setOrder(order)).collect(Collectors.toList()));
        cart.setItems(null);
        cartsRepository.save(cart);
    }

    public List<CartGetDTO> getAllCarts() {
        List<Carts> carts = cartsRepository.findAll();
        if (carts.isEmpty()) {
            throw new CustomError(4004, "No hay carritos");
        }
        return carts.stream().map(cart -> CartGetDTO.mapToDto(cart)).collect(Collectors.toList());
    }

    public CartGetDTO getCartById(long id) {
        return CartGetDTO.mapToDto(cartsRepository.findById(id).orElseThrow(
            () -> new CustomError(4004, "Carrito no encontrado")
        ));
    }
    
}
