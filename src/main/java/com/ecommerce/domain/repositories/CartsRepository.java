package com.ecommerce.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.domain.models.Carts;

@Repository
public interface CartsRepository extends JpaRepository<Carts, Long> {
    public Optional<Carts> findByUserId(long userId); // encontrar carrito por id de usuario
}