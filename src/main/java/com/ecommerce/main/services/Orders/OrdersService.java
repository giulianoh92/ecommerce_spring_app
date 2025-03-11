package com.ecommerce.main.services.Orders;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.main.domain.models.Orders;
import com.ecommerce.main.domain.models.Statuses;
import com.ecommerce.main.domain.repositories.OrdersRepository;
import com.ecommerce.main.domain.repositories.StatusesRepository;
import com.ecommerce.main.error.CustomError;
import com.ecommerce.main.services.Orders.dto.OrderGetDTO;
import com.ecommerce.main.services.Orders.dto.OrderUpdateDTO;


/**
 * OrdersService
 * esta clase se encarga de manejar la lógica de negocio relacionada con los pedidos
 * contiene métodos para obtener, crear, actualizar y eliminar pedidos
 * también contiene métodos para obtener y actualizar los estados de los pedidos
 * y para obtener los estados disponibles
 */
@Service
public class OrdersService {

    @Autowired
    private final OrdersRepository ordersRepository;

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
        StatusesRepository statusesRepository
    ) {
        this.ordersRepository = ordersRepository;
        this.statusesRepository = statusesRepository;
    }

    public List<OrderGetDTO> getAll() {
        List<Orders> orders = ordersRepository.findAll();
        if (orders.isEmpty()) {
            throw new CustomError(4004, "No hay pedidos registrados");
        }
        return orders.stream().map(order -> OrderGetDTO.mapToDto(order)).collect(Collectors.toList());
    }

    public List<Orders> getAllEntity() {
        List<Orders> orders = ordersRepository.findAll();
        if (orders.isEmpty()) {
            throw new CustomError(4004, "No hay pedidos registrados");
        }
        return orders;
    }

    public OrderGetDTO getById(long id) {
        return OrderGetDTO.mapToDto(ordersRepository.findById(id).
        orElseThrow(
            () -> new CustomError(
                4004, 
                "Pedido no encontrado"
            )
        ));
    }

    public List<OrderGetDTO> getByUserId(long userId) {
        List<Orders> orders = ordersRepository.findByUserId(userId);
        if (orders.isEmpty()) {
            throw new CustomError(4004, "No hay pedidos registrados");
        }
        return orders.stream().map(order -> OrderGetDTO.mapToDto(order)).collect(Collectors.toList());
    }

    public void updateStatus(long orderId, String statusKey) {
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

    public void update(Long id, OrderUpdateDTO orderUpdateDTO) {
        Orders order = ordersRepository.findById(id).orElseThrow(
            () -> new CustomError(4004, "Pedido no encontrado")
        );
        if (orderUpdateDTO.getStatusId() != null) {
            Statuses status = statusesRepository.findById(orderUpdateDTO.getStatusId()).orElseThrow(
                () -> new CustomError(4004, "Estado no encontrado")
            );
            order.setStatus(status);
        }
        ordersRepository.save(order);
    }

    public List<Statuses> getAllStatuses() {
        return statusesRepository.findAll();
    }

    public void createStandardStatuses() {
        for (String statusName : statusMap.values()) {
            if (statusesRepository.findByName(statusName).isEmpty()) {
                Statuses status = new Statuses();
                status.setName(statusName);
                statusesRepository.save(status);
            }
        }
    }
}
