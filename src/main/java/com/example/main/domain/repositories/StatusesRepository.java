package com.example.main.domain.repositories;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.main.domain.models.Statuses;

@Repository
public interface StatusesRepository extends JpaRepository<Statuses, Long> {
    public Optional<Statuses> findByName(String name);
}  
