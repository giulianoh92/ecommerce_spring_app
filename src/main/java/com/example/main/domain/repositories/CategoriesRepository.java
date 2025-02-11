package com.example.main.domain.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.main.domain.models.Categories;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    Object findByName(String name); // encontrar categoria por nombre
}