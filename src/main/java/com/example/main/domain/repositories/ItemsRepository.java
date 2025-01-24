package com.example.main.domain.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.main.domain.models.Items;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Long> {
}
