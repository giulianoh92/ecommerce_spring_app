package com.ecommerce.main.domain.repositories;

import org.springframework.stereotype.Repository;

import com.ecommerce.main.domain.models.Categories;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    Object findByName(String name); // encontrar categoria por nombre
}