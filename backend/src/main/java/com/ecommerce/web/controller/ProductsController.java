package com.ecommerce.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.main.controllers.MainController;
import com.ecommerce.main.domain.models.Categories;
import com.ecommerce.main.services.Products.dto.CategoryCreateDTO;
import com.ecommerce.main.services.Products.dto.ProductCreateDTO;
import com.ecommerce.main.services.Products.dto.ProductGetDTO;
import com.ecommerce.main.services.Products.dto.ProductUpdateDTO;

import jakarta.validation.Valid;

/**
 * Clase controladora para la creación de endpoints HTTP
 * permite la creación de endpoints para la comunicación con el frontend
 * 
 */
@Validated
@RestController
@RequestMapping("/products")
public class ProductsController {
    
    @Autowired
    private MainController mainController;

    @GetMapping
    public ResponseEntity<List<ProductGetDTO>> getAllProducts(
        @RequestParam int page,
        @RequestParam int limit,
        @RequestParam(required = false) String q,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(required = false) Boolean inStock,
        @RequestParam(required = false) Boolean active,
        @RequestParam(required = false, defaultValue = "id") String sortBy,
        @RequestParam(required = false, defaultValue = "asc") String order
    ) {
        List<ProductGetDTO> products = mainController.getAllProducts(page, limit, q, categoryId, minPrice, maxPrice, inStock, active, sortBy, order);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductGetDTO> getProductById(@PathVariable long id) {
        ProductGetDTO product = mainController.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductCreateDTO product) {
        mainController.createProduct(product);
        return new ResponseEntity<>("Producto creado con éxito", HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable long id, @Valid @RequestBody ProductUpdateDTO product) {
        mainController.updateProduct(id, product);
        return new ResponseEntity<>("Producto actualizado con éxito", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable long id) {
        mainController.deleteProduct(id);
        return new ResponseEntity<>("Producto eliminado con éxito", HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Categories>> getAllCategories() {
        List<Categories> categories = mainController.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<Categories> getCategoryById(@PathVariable long id) {
        Categories category = mainController.getCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping("/categories")
    public ResponseEntity<Object> createCategory(@RequestBody @Valid CategoryCreateDTO category) {
        mainController.createCategory(category);
        return new ResponseEntity<>("Categoría creada con éxito", HttpStatus.CREATED);
    }
}
