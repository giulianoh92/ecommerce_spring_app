package com.ecommerce.main.domain.repositories;

import org.springframework.stereotype.Repository;

import com.ecommerce.main.domain.models.Items;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Long> {
}
