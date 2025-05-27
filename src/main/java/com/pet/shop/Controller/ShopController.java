package com.pet.shop.Controller;

import com.pet.shop.Model.Category;
import com.pet.shop.Model.Product;
import com.pet.shop.Service.ShopService;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
@Tag(name = "Shop Controller", description = "API для управления товарами и категориями")
@RestController
@RequestMapping("/api/v1/shop")
@AllArgsConstructor
public class ShopController {

    private final Logger log = LoggerFactory.getLogger(ShopController.class);
    private ShopService shopService;


    @Operation(summary = "Сохранить продукт", description = "Создаёт или обновляет продукт в магазине")
    @PostMapping("save")
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        log.info("Received request to save product: {}", product);
        try {
            Product savedProduct = shopService.createOrUpdate(product);
            log.info("Successfully saved product: {}", savedProduct);
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            log.error("Error saving product {}", product, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    @Operation(summary = "Сохранить продукы", description = "Создаёт или обновляет список продуктов в магазине")
    @PostMapping("saveAll")
    public ResponseEntity<List<Product>> saveAllProduct(@RequestBody List<Product> products) {
        log.info("Received request to save a list of products: {}", products.size());
        List<Product> savedProducts = new LinkedList<>();
        try {
            for (Product p : products) {
                Product savedProduct = shopService.createOrUpdate(p);
                savedProducts.add(savedProduct);
                log.info("Successfully saved product: {}", p);
            }
            return ResponseEntity.ok(savedProducts);
        } catch (Exception e) {
            log.error("Error saving products list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    @Operation(summary = "Получить все продукты", description = "Возвращает список всех продуктов")
    @GetMapping("findAll")
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("Received request to get all products");
        try {
            List<Product> products = shopService.getAllProducts();
            log.info("Found {} products", products.size());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("Error retrieving all products", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    @Operation(summary = "Получить продукт", description = "Возвращает список найденных продуктов по query")
    @GetMapping("find")
    public ResponseEntity<List<Product>> getProducts(@RequestParam String query) {
        log.info("Received request to search for products with query: {}", query);

        Category category = tryParseCategory(query);
        String[] keywords = query.split(" ");

        List<Product> products;
        try {
            if (category != null) {
                log.info("Searching products by category: {}", category);
                products = shopService.getProductsByCategory(category);
            } else {
                log.info("Searching products by keywords: {}", (Object[]) keywords);
                products = shopService.getProductsBySearch(keywords);
            }

            if (products.isEmpty()) {
                log.warn("No products found for query: {}", query);
                return ResponseEntity.notFound().build();
            }

            log.info("Found {} products for query: {}", products.size(), query);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("Error occurred while searching for products with query: {}", query, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    private Category tryParseCategory(String query) {
        try {
            return Category.valueOf(query.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.debug("Invalid category '{}', returning null", query);
            return null;
        }
    }
}
