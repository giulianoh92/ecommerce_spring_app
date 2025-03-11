package com.ecommerce.main.domain.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.main.domain.models.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    public List<Orders> findByUserId(long userId); // encontrar pedidos por id de usuario
}
