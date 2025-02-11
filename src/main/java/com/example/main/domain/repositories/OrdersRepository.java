package com.example.main.domain.repositories;

import com.example.main.domain.models.Orders;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    public List<Orders> findByUserId(long userId); // encontrar pedidos por id de usuario
}
