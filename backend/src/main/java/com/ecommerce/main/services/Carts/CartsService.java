package com.ecommerce.main.services.Carts;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.main.domain.models.Carts;
import com.ecommerce.main.domain.models.Items;
import com.ecommerce.main.domain.models.Orders;
import com.ecommerce.main.domain.models.Products;
import com.ecommerce.main.domain.models.Statuses;
import com.ecommerce.main.domain.models.Users;
import com.ecommerce.main.domain.repositories.CartsRepository;
import com.ecommerce.main.domain.repositories.ItemsRepository;
import com.ecommerce.main.domain.repositories.OrdersRepository;
import com.ecommerce.main.domain.repositories.ProductsRepository;
import com.ecommerce.main.domain.repositories.StatusesRepository;
import com.ecommerce.main.domain.repositories.UsersRepository;
import com.ecommerce.main.error.CustomError;
import com.ecommerce.main.services.Carts.dto.CartGetDTO;


/*
 * CartsService
 * esta clase se encarga de manejar la lógica de negocio relacionada con los carritos
 * contiene métodos para obtener, crear, actualizar y eliminar carritos
 * también contiene métodos para agregar, actualizar y eliminar items de un carrito
 * y para realizar el checkout de un carrito
 */
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

    @Autowired
    private final StatusesRepository statusesRepository;

    public CartsService(
        CartsRepository cartsRepository,
        ProductsRepository productsRepository,
        UsersRepository usersRepository,
        OrdersRepository ordersRepository,
        ItemsRepository itemsRepository,
        StatusesRepository statusesRepository
    ) {
        this.cartsRepository = cartsRepository;
        this.productsRepository = productsRepository;
        this.usersRepository = usersRepository;
        this.ordersRepository = ordersRepository;
        this.itemsRepository = itemsRepository;
        this.statusesRepository = statusesRepository;
    }

    public void createCart(long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(
            () -> new CustomError(4004, "Usuario no encontrado")
        );
        Carts cart = new Carts(user);
        cartsRepository.save(cart);
    }

    public void addItemToCart(long userId, long productId, int quantity) {

        Products product = productsRepository.findById(productId).orElseThrow(
            () -> new CustomError(4004, "Producto no encontrado")
        );

        if (quantity <= 0) {
            throw new CustomError(4004, "La cantidad debe ser mayor a 0");
        }

        if (product.getStock() < quantity) {
            throw new CustomError(4004, "No hay suficiente stock");
        }

        Carts cart = cartsRepository.findByUserId(userId).orElseThrow(
            () -> new CustomError(4004, "Carrito no encontrado")
        );
        
        // Verifica si el item ya existe en el carrito
        Items existingItem = cart.getItems().stream()
            .filter(item -> item.getProduct().getId() == productId)
            .findFirst()
            .orElse(null);
        
        if (existingItem != null) {
            // Si el item ya existe, actualiza la cantidad
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            if (product.getStock() < existingItem.getQuantity()) {
                throw new CustomError(4004, "No hay suficiente stock");
            }
            itemsRepository.save(existingItem);
        } else {
            // Si el item no existe, crea uno nuevo
            Items newItem = new Items(productsRepository.findById(productId).orElseThrow(
                () -> new CustomError(4004, "Producto no encontrado")
            ), quantity);
            newItem.setCart(cart); // Asocia el item con el carrito
            itemsRepository.save(newItem); // Guarda el item en la base de datos
            cart.addItem(newItem);
        }
        
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
        Products product = productsRepository.findById(productId).orElseThrow(
            () -> new CustomError(4004, "Producto no encontrado")
        );
        if (quantity <= 0) {
            throw new CustomError(4004, "La cantidad debe ser mayor a 0");
        }
        if (product.getStock() < quantity) {
            throw new CustomError(4004, "No hay suficiente stock");
        }
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
        Carts cart = cartsRepository.findByUserId(userId).orElseThrow(
            () -> new CustomError(4004, "Carrito no encontrado")
        );
        if (cart.getItems().isEmpty()) {
            throw new CustomError(4001, "El carrito está vacío");
        }
        Statuses status = statusesRepository.findByName("Pendiente").orElseGet(() -> statusesRepository.save(new Statuses("Pendiente")));
        Orders order = new Orders();
        order.setTotal(cart.getTotal());
        order.setUser(cart.getUser());
        order.setStatus(status);
    
        // Guarda la instancia de Orders primero
        Orders newOrder = ordersRepository.save(order);
    
        // Actualiza los Items existentes para que apunten al nuevo Orders y desasocien el Carts
        List<Items> orderItems = cart.getItems().stream()
            .peek(item -> {
                item.setOrder(newOrder);
                item.setCart(null);
    
                // Actualiza el stock del producto
                Products product = item.getProduct();
                int newStock = product.getStock() - item.getQuantity();
                if (newStock < 0) {
                    throw new CustomError(4004, "Stock insuficiente para el producto: " + product.getName());
                }
                product.setStock(newStock);
                productsRepository.save(product);
            })
            .collect(Collectors.toList());
        itemsRepository.saveAll(orderItems);
    
        // Actualiza el carrito para que no tenga items
        cart.setItems(null);
        cartsRepository.save(cart);
    
        ordersRepository.save(newOrder);
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
    
    public CartGetDTO getUserCart(long userId) {
        return CartGetDTO.mapToDto(cartsRepository.findByUserId(userId).orElseThrow(
            () -> new CustomError(4004, "Carrito no encontrado")
        ));
    }
}