package com.example.main.services.Orders;

import org.springframework.stereotype.Service;

import com.example.main.domain.repositories.ItemsRepository;
import com.example.main.domain.repositories.OrdersRepository;
import com.example.main.domain.repositories.StatusesRepository;

@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;

    private final ItemsRepository itemsRepository;

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

    
    
}
