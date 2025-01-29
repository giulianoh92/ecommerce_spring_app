package com.example.main.services.Products;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.main.domain.models.Categories;
import com.example.main.domain.models.Products;
import com.example.main.domain.repositories.CategoriesRepository;
import com.example.main.domain.repositories.ProductsRepository;
import com.example.main.error.CustomError;
import com.example.main.services.Products.dto.CategoryCreateDTO;
import com.example.main.services.Products.dto.ProductCreateDTO;
import com.example.main.services.Products.dto.ProductGetDTO;
import com.example.main.services.Products.dto.ProductUpdateDTO;

@Service
public class ProductsService {

    @Autowired
    private final ProductsRepository productsRepository;

    @Autowired
    private final CategoriesRepository categoriesRepository;

    public ProductsService(
        ProductsRepository productsRepository,
        CategoriesRepository categoriesRepository
    ) {
        this.productsRepository = productsRepository;
        this.categoriesRepository = categoriesRepository;
    }

    public List<ProductGetDTO> getAll() {
        List<Products> products = productsRepository.findAll();
        if (products.isEmpty()) {
            throw new CustomError(4004, "No hay productos registrados");
        }

        return products.stream().map(product -> ProductGetDTO.mapToDto(product)).collect(Collectors.toList());
    }

    public ProductGetDTO getById(long id) {
        return ProductGetDTO.mapToDto(productsRepository.findById(id).
        orElseThrow(
            () -> new CustomError(
                4004, 
                "Producto no encontrado"
            )
        ));
    }

    public void create(ProductCreateDTO product) {
        if(productsRepository.findByName(product.getName()) != null) {
            throw new CustomError(4000, "El producto ya está registrado");
        }
        Products newProduct = new Products(
            product.getName(),
            product.getDescription(),
            product.getUnitPrice(),
            product.getStock(),
            product.getImageUrl(),
            categoriesRepository.findById(product.getCategoryId()).orElseThrow(
                () -> new CustomError(
                    4004, 
                    "Categoría no encontrada"
                )
            )
        );
        productsRepository.save(newProduct);
    }

    public void update(long id, ProductUpdateDTO product) {
        Products productModel = productsRepository.findById(id).
        orElseThrow(
            () -> new CustomError(
                4004, 
                "Producto no encontrado"
            )
        );
        if (product.getName() != null){
            productModel.setName(product.getName());
        }
        if (product.getDescription() != null){
            productModel.setDescription(product.getDescription());
        }
        if (product.getUnitPrice() != null){
            productModel.setUnitPrice(product.getUnitPrice());
        }
        if (product.getStock() != null){
            productModel.setStock(product.getStock());
        }
        if (product.getImageUrl() != null){
            productModel.setImageUrl(product.getImageUrl());
        }
        if (product.getCategoryId() != null){
            productModel.setCategory(categoriesRepository.findById(product.getCategoryId()).orElseThrow(
                () -> new CustomError(
                    4004, 
                    "Categoría no encontrada"
                )
            ));
        }
        productsRepository.save(productModel);
    }

    public void delete(long id) {
        Products product = productsRepository.findById(id).
        orElseThrow(
            () -> new CustomError(
                4004, 
                "Producto no encontrado"
            )
        );
        productsRepository.delete(product);
    }

    public List<Categories> getAllCategories() {
        List<Categories> categories = categoriesRepository.findAll();
        if (categories.isEmpty()) {
            throw new CustomError(4004, "No hay categorías registradas");
        }

        return categories;
    }

    public Categories getCategoryById(long id) {
        return categoriesRepository.findById(id).
        orElseThrow(
            () -> new CustomError(
                4004, 
                "Categoría no encontrada"
            )
        );
    }

    public void createCategory(CategoryCreateDTO category) {
        if(categoriesRepository.findByName(category.getName()) != null) {
            throw new CustomError(4000, "La categoría ya está registrada");
        }
        Categories newCategory = new Categories(
            category.getName()
        );
        categoriesRepository.save(newCategory);
    }
    
}
