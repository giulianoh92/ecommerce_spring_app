package com.ecommerce.main.services.Products.dto;

import com.ecommerce.main.domain.models.Products;

public class ProductGetDTO {
    private long id;
    private String name;
    private String description;
    private double unitPrice;
    private int stock;
    private String category;

    public ProductGetDTO() {
    }

    public ProductGetDTO(long id, String name, String description, double unitPrice, int stock, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.stock = stock;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public static ProductGetDTO mapToDto(Products product) {
        return new ProductGetDTO(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getUnitPrice(),
            product.getStock(),
            product.getCategory().getName()
        );
    }
}
