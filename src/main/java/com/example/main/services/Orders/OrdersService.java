package com.example.main.services.Orders;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.main.domain.models.Orders;
import com.example.main.domain.repositories.ItemsRepository;
import com.example.main.domain.repositories.OrdersRepository;
import com.example.main.domain.repositories.StatusesRepository;
import com.example.main.error.CustomError;

@Service
public class OrdersService {

    @Autowired
    private final OrdersRepository ordersRepository;

    @Autowired
    private final ItemsRepository itemsRepository;

    @Autowired
    private final StatusesRepository statusesRepository;

    public OrdersService(
        OrdersRepository ordersRepository,
        ItemsRepository itemsRepository,
        StatusesRepository statusesRepository
    ) {
        this.ordersRepository = ordersRepository;
        this.itemsRepository = itemsRepository;
        this.statusesRepository = statusesRepository;
    }

    public List<Orders> getAllOrders() {
        List<Orders> orders = ordersRepository.findAll();
        if (orders.isEmpty()) {
            throw new CustomError(4004, "No hay pedidos registrados");
        }
        return orders;
    }

    public Orders getOrderById(long id) {
        return ordersRepository.findById(id).
        orElseThrow(
            () -> new CustomError(
                4004, 
                "Pedido no encontrado"
            )
        );
    }

    public List<Orders> getOrdersByUserId(long userId) {
        List<Orders> orders = ordersRepository.findByUserId(userId);
        if (orders.isEmpty()) {
            throw new CustomError(4004, "No hay pedidos registrados");
        }
        return orders;
    }

    public void updateOrderStatus(long orderId, long statusId) {
        Orders order = ordersRepository.findById(orderId).orElseThrow(
            () -> new CustomError(4004, "Pedido no encontrado")
        );
        order.setStatus(statusesRepository.findById(statusId).orElseThrow(
            () -> new CustomError(4004, "Estado no encontrado")
        ));
        ordersRepository.save(order);
    }
}
