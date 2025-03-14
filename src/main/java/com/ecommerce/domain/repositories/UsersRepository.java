package com.ecommerce.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.domain.models.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email); // encontrar usuario por email
    Users findByEmailAndPassword(String email, String password); // encontrar usuario por email y contraseña
}