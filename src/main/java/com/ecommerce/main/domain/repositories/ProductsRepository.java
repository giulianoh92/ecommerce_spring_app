package com.ecommerce.main.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.main.domain.models.Products;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
    Products findByName(String name);

    // encontrar productos con parametros de paginacion, orden y filtro
    Page<Products> findByNameContainingAndCategoryIdAndUnitPriceBetweenAndStockGreaterThanAndActiveTrue(
        String name, Long categoryId, Double minPrice, Double maxPrice, int stock, Pageable pageable);

    // encontrar productos con parametros de paginacion, orden y filtro (sin categoria)
    Page<Products> findByNameContainingAndUnitPriceBetweenAndStockGreaterThanAndActiveTrue(
        String name, Double minPrice, Double maxPrice, int stock, Pageable pageable);
}