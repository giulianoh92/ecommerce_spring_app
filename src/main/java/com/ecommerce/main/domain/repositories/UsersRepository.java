package com.ecommerce.main.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.main.domain.models.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email); // encontrar usuario por email
    Users findByEmailAndPassword(String email, String password); // encontrar usuario por email y contraseña
}