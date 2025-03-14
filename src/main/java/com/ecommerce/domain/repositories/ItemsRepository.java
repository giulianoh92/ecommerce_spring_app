package com.ecommerce.domain.repositories;

import org.springframework.stereotype.Repository;

import com.ecommerce.domain.models.Items;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Long> {
}
