package com.example.main.domain.repositories;

import com.example.main.domain.models.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
    Products findByName(String name);

    Page<Products> findByNameContainingAndCategoryIdAndUnitPriceBetweenAndStockGreaterThanAndActiveTrue(
        String name, Long categoryId, Double minPrice, Double maxPrice, int stock, Pageable pageable);

    Page<Products> findByNameContainingAndUnitPriceBetweenAndStockGreaterThanAndActiveTrue(
        String name, Double minPrice, Double maxPrice, int stock, Pageable pageable);
}