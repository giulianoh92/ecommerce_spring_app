package com.ecommerce.main.services.Products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductCreateDTO {
    @NotNull(message = "El nombre es requerido")
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 5, max = 50, message = "El nombre debe tener entre 5 y 50 caracteres")
    private String name;

    @NotNull(message = "La descripción es requerida")
    @NotBlank(message = "La descripción es requerida")
    @Size(min = 5, max = 255, message = "La descripción debe tener entre 5 y 255 caracteres")
    private String description;

    @NotNull(message = "El precio unitario es requerido")
    private Double unitPrice;

    @NotNull(message = "El stock es requerido")
    private Integer stock;

    private String imageUrl;

    @NotNull(message = "La categoría es requerida")
    private Long categoryId;

    public ProductCreateDTO() {
    }

    public ProductCreateDTO(String name, String description, Double unitPrice, Integer stock, String imageUrl, Long categoryId) {
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
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

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

}
