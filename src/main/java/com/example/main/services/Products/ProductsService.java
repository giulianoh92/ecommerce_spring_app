package com.example.main.services.Products;

import java.util.ArrayList;
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

        return products.stream()
            .filter(product -> product.isActive())
            .sorted((p1, p2) -> Long.compare(p1.getId(), p2.getId()))
            .map(product -> ProductGetDTO.mapToDto(product))
            .collect(Collectors.toList());
    }

    public List<ProductGetDTO> getAllForShop() {
        List<Products> products = productsRepository.findAll();
        if (products.isEmpty()) {
            throw new CustomError(4004, "No hay productos registrados");
        }

        return products.stream()
            .filter(product -> product.isActive() && product.getStock() > 0)
            .sorted((p1, p2) -> Long.compare(p1.getId(), p2.getId()))
            .map(product -> ProductGetDTO.mapToDto(product))
            .collect(Collectors.toList());
    }

    public List<Products> getAllEntity() {
        List<Products> products = productsRepository.findAll();
        if (products.isEmpty()) {
            throw new CustomError(4004, "No hay productos registrados");
        }

        return products;
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
        product.setActive(false);
        productsRepository.save(product);
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

    public void populateDatabaseWithProducts() {
        List<Categories> categories = new ArrayList<Categories>();
        categories.add(new Categories("Electrónicos"));
        categories.add(new Categories("Hogar"));
        categories.add(new Categories("Deportes"));
        categories.add(new Categories("Juguetes"));
        categories.add(new Categories("Ropa"));
        categories.add(new Categories("Calzado"));
        categories.add(new Categories("Accesorios"));
        categories.add(new Categories("Libros"));

        categoriesRepository.saveAll(categories);

        List<Products> products = new ArrayList<Products>();
        products.add(new Products("Laptop", "Laptop HP", 500.00, 10, "https://www.google.com", categories.get(0)));
        products.add(new Products("Smartphone", "Smartphone Samsung", 300.00, 20, "https://www.google.com", categories.get(0)));
        products.add(new Products("TV", "TV LG", 400.00, 15, "https://www.google.com", categories.get(0)));
        products.add(new Products("Sofá", "Sofá de 3 plazas", 200.00, 5, "https://www.google.com", categories.get(1)));
        products.add(new Products("Mesa", "Mesa de centro", 100.00, 10, "https://www.google.com", categories.get(1)));
        products.add(new Products("Bicicleta", "Bicicleta de montaña", 150.00, 8, "https://www.google.com", categories.get(2)));
        products.add(new Products("Balón", "Balón de fútbol", 20.00, 30, "https://www.google.com", categories.get(2)));
        products.add(new Products("Muñeca", "Muñeca Barbie", 10.00, 50, "https://www.google.com", categories.get(3)));
        products.add(new Products("Camisa", "Camisa de vestir", 30.00, 25, "https://www.google.com", categories.get(4)));
        products.add(new Products("Zapatos", "Zapatos de cuero", 40.00, 20, "https://www.google.com", categories.get(5)));
        products.add(new Products("Reloj", "Reloj de pulsera", 50.00, 15, "https://www.google.com", categories.get(6)));
        products.add(new Products("Lentes", "Lentes de sol", 15.00, 40, "https://www.google.com", categories.get(6)));
        products.add(new Products("Libro", "Libro de ciencia ficción", 5.00, 100, "https://www.google.com", categories.get(7)));

        productsRepository.saveAll(products);
    }
    
}
