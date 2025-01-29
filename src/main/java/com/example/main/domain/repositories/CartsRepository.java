package com.example.main.domain.repositories;

import com.example.main.domain.models.Carts;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartsRepository extends JpaRepository<Carts, Long> {
    public Optional<Carts> findByUserId(long userId);
}