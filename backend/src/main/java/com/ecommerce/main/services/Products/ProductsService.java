package com.ecommerce.main.services.Products;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ecommerce.main.domain.models.Categories;
import com.ecommerce.main.domain.models.Products;
import com.ecommerce.main.domain.repositories.CategoriesRepository;
import com.ecommerce.main.domain.repositories.ProductsRepository;
import com.ecommerce.main.error.CustomError;
import com.ecommerce.main.services.Products.dto.CategoryCreateDTO;
import com.ecommerce.main.services.Products.dto.ProductCreateDTO;
import com.ecommerce.main.services.Products.dto.ProductGetDTO;
import com.ecommerce.main.services.Products.dto.ProductUpdateDTO;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


/*
 * ProductsService
 * esta clase se encarga de manejar la lógica de negocio relacionada con los productos
 * contiene métodos para obtener, crear, actualizar y eliminar productos
 * también contiene métodos para obtener categorías y crear nuevas categorías
 * además, contiene un método para poblar la base de datos con productos de ejemplo
 */
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

    public List<ProductGetDTO> getAll(int page, int limit, String q, Long categoryId, Double minPrice, Double maxPrice, Boolean inStock, Boolean active, String sortBy, String order) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.fromString(order), sortBy));
        Page<Products> productsPage;

        if (categoryId != null) {
            productsPage = productsRepository.findByNameContainingAndCategoryIdAndUnitPriceBetweenAndStockGreaterThanAndActiveTrue(
                q != null ? q : "", 
                categoryId, 
                minPrice != null ? minPrice : 0.0, 
                maxPrice != null ? maxPrice : Double.MAX_VALUE, 
                inStock != null && inStock ? 0 : -1, 
                pageable
            );
        } else {
            productsPage = productsRepository.findByNameContainingAndUnitPriceBetweenAndStockGreaterThanAndActiveTrue(
                q != null ? q : "", 
                minPrice != null ? minPrice : 0.0, 
                maxPrice != null ? maxPrice : Double.MAX_VALUE, 
                inStock != null && inStock ? 0 : -1, 
                pageable
            );
        }

        return productsPage.getContent().stream()
            .map(ProductGetDTO::mapToDto)
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
        List<Categories> categories = new ArrayList<>();
        categories.add(new Categories("Electrónicos"));
        categories.add(new Categories("Hogar"));
        categories.add(new Categories("Deportes"));
        categories.add(new Categories("Juguetes"));
        categories.add(new Categories("Ropa"));
        categories.add(new Categories("Calzado"));
        categories.add(new Categories("Accesorios"));
        categories.add(new Categories("Libros"));
    
        categoriesRepository.saveAll(categories);
    
        List<ProductCreateDTO> products = new ArrayList<>();
        products.add(new ProductCreateDTO("Laptop", "Laptop HP", 500.00, 10, "https://www.google.com", categories.get(0).getId()));
        products.add(new ProductCreateDTO("Smartphone", "Smartphone Samsung", 300.00, 20, "https://www.google.com", categories.get(0).getId()));
        products.add(new ProductCreateDTO("TV", "TV LG", 400.00, 15, "https://www.google.com", categories.get(0).getId()));
        products.add(new ProductCreateDTO("Sofá", "Sofá de 3 plazas", 200.00, 5, "https://www.google.com", categories.get(1).getId()));
        products.add(new ProductCreateDTO("Mesa", "Mesa de centro", 100.00, 10, "https://www.google.com", categories.get(1).getId()));
        products.add(new ProductCreateDTO("Bicicleta", "Bicicleta de montaña", 150.00, 8, "https://www.google.com", categories.get(2).getId()));
        products.add(new ProductCreateDTO("Balón", "Balón de fútbol", 20.00, 30, "https://www.google.com", categories.get(2).getId()));
        products.add(new ProductCreateDTO("Muñeca", "Muñeca Barbie", 10.00, 50, "https://www.google.com", categories.get(3).getId()));
        products.add(new ProductCreateDTO("Camisa", "Camisa de vestir", 30.00, 25, "https://www.google.com", categories.get(4).getId()));
        products.add(new ProductCreateDTO("Zapatos", "Zapatos de cuero", 40.00, 20, "https://www.google.com", categories.get(5).getId()));
        products.add(new ProductCreateDTO("Reloj", "Reloj de pulsera", 50.00, 15, "https://www.google.com", categories.get(6).getId()));
        products.add(new ProductCreateDTO("Lentes", "Lentes de sol", 15.00, 40, "https://www.google.com", categories.get(6).getId()));
        products.add(new ProductCreateDTO("Libro", "Libro de ciencia ficción", 5.00, 100, "https://www.google.com", categories.get(7).getId()));
    
        // Adding 20 more sample products
        products.add(new ProductCreateDTO("Tablet", "Tablet Apple", 600.00, 12, "https://www.google.com", categories.get(0).getId()));
        products.add(new ProductCreateDTO("Monitor", "Monitor Dell", 250.00, 18, "https://www.google.com", categories.get(0).getId()));
        products.add(new ProductCreateDTO("Teclado", "Teclado mecánico", 80.00, 30, "https://www.google.com", categories.get(0).getId()));
        products.add(new ProductCreateDTO("Mouse", "Mouse inalámbrico", 40.00, 25, "https://www.google.com", categories.get(0).getId()));
        products.add(new ProductCreateDTO("Impresora", "Impresora HP", 150.00, 10, "https://www.google.com", categories.get(0).getId()));
        products.add(new ProductCreateDTO("Cámara", "Cámara Canon", 700.00, 8, "https://www.google.com", categories.get(0).getId()));
        products.add(new ProductCreateDTO("Auriculares", "Auriculares Sony", 120.00, 20, "https://www.google.com", categories.get(0).getId()));
        products.add(new ProductCreateDTO("Altavoces", "Altavoces Bose", 300.00, 15, "https://www.google.com", categories.get(0).getId()));
        products.add(new ProductCreateDTO("Microondas", "Microondas LG", 100.00, 10, "https://www.google.com", categories.get(1).getId()));
        products.add(new ProductCreateDTO("Refrigerador", "Refrigerador Samsung", 800.00, 5, "https://www.google.com", categories.get(1).getId()));
        products.add(new ProductCreateDTO("Lavadora", "Lavadora Whirlpool", 600.00, 7, "https://www.google.com", categories.get(1).getId()));
        products.add(new ProductCreateDTO("Tostadora", "Tostadora Philips", 50.00, 20, "https://www.google.com", categories.get(1).getId()));
        products.add(new ProductCreateDTO("Licuadora", "Licuadora Oster", 70.00, 15, "https://www.google.com", categories.get(1).getId()));
        products.add(new ProductCreateDTO("Cafetera", "Cafetera Nespresso", 150.00, 10, "https://www.google.com", categories.get(1).getId()));
        products.add(new ProductCreateDTO("Aspiradora", "Aspiradora Dyson", 400.00, 8, "https://www.google.com", categories.get(1).getId()));
        products.add(new ProductCreateDTO("Cinta de correr", "Cinta de correr ProForm", 1000.00, 5, "https://www.google.com", categories.get(2).getId()));
        products.add(new ProductCreateDTO("Pesas", "Pesas ajustables", 200.00, 20, "https://www.google.com", categories.get(2).getId()));
        products.add(new ProductCreateDTO("Raqueta", "Raqueta de tenis", 150.00, 15, "https://www.google.com", categories.get(2).getId()));
        products.add(new ProductCreateDTO("Patines", "Patines en línea", 100.00, 25, "https://www.google.com", categories.get(2).getId()));
        products.add(new ProductCreateDTO("Casco", "Casco de ciclismo", 80.00, 30, "https://www.google.com", categories.get(2).getId()));
    
        for (ProductCreateDTO product : products) {
            this.create(product);
        }
    }
    
}
