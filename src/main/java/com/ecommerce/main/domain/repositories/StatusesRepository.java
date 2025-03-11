package com.ecommerce.main.domain.repositories;

import org.springframework.stereotype.Repository;

import com.ecommerce.main.domain.models.Statuses;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface StatusesRepository extends JpaRepository<Statuses, Long> {
    public Optional<Statuses> findByName(String name);
}  
