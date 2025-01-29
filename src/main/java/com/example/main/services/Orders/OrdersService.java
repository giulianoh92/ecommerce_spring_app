package com.example.main.services.Orders;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.main.domain.models.Orders;
import com.example.main.domain.models.Statuses;
import com.example.main.domain.repositories.ItemsRepository;
import com.example.main.domain.repositories.OrdersRepository;
import com.example.main.domain.repositories.StatusesRepository;
import com.example.main.error.CustomError;
import com.example.main.services.Orders.dto.OrderGetDTO;

@Service
public class OrdersService {

    @Autowired
    private final OrdersRepository ordersRepository;

    @Autowired
    private final ItemsRepository itemsRepository;

    @Autowired
    private final StatusesRepository statusesRepository;

    private final Map<String, String> statusMap = Map.of(
        "pending", "Pendiente",
        "confirmed", "Confirmado",
        "in_transit", "En tránsito",
        "delivered", "Entregado",
        "cancelled", "Cancelado"
    );

    public OrdersService(
        OrdersRepository ordersRepository,
        ItemsRepository itemsRepository,
        StatusesRepository statusesRepository
    ) {
        this.ordersRepository = ordersRepository;
        this.itemsRepository = itemsRepository;
        this.statusesRepository = statusesRepository;
    }

    public List<OrderGetDTO> getAllOrders() {
        List<Orders> orders = ordersRepository.findAll();
        if (orders.isEmpty()) {
            throw new CustomError(4004, "No hay pedidos registrados");
        }
        return orders.stream().map(order -> OrderGetDTO.mapToDto(order)).collect(Collectors.toList());
    }

    public OrderGetDTO getOrderById(long id) {
        return OrderGetDTO.mapToDto(ordersRepository.findById(id).
        orElseThrow(
            () -> new CustomError(
                4004, 
                "Pedido no encontrado"
            )
        ));
    }

    public List<OrderGetDTO> getOrdersByUserId(long userId) {
        List<Orders> orders = ordersRepository.findByUserId(userId);
        if (orders.isEmpty()) {
            throw new CustomError(4004, "No hay pedidos registrados");
        }
        return orders.stream().map(order -> OrderGetDTO.mapToDto(order)).collect(Collectors.toList());
    }

    public void updateOrderStatus(long orderId, String statusKey) {
        String statusName = statusMap.get(statusKey);
        if (statusName == null) {
            throw new CustomError(4004, "Estado no válido");
        }
        Statuses status = statusesRepository.findByName(statusName).orElseGet(() -> {
            Statuses newStatus = new Statuses();
            newStatus.setName(statusName);
            return statusesRepository.save(newStatus);
        });
        Orders order = ordersRepository.findById(orderId).orElseThrow(
            () -> new CustomError(4004, "Pedido no encontrado")
        );
        order.setStatus(status);
        ordersRepository.save(order);
    }
}
