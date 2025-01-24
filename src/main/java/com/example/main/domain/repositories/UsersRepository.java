package com.example.main.domain.repositories;

import com.example.main.domain.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
    Users findByEmailAndPassword(String email, String password);
}