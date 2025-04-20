package com.pet.shop.Controller;


import com.pet.shop.Model.Category;
import com.pet.shop.Model.Product;
import com.pet.shop.Service.ShopService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shop")
@AllArgsConstructor
public class ShopController {

    private ShopService shopService;

    @PostMapping("save")
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        return ResponseEntity.ok(shopService.createOrUpdate(product));
    }

    @PostMapping("saveAll")
    public ResponseEntity<List<Product>> saveAllProduct(@RequestBody List<Product> product) {
        List<Product> ans = new LinkedList<>();
        for (Product p : product) {
            shopService.createOrUpdate(p);
            ans.add(p);
        }
        return ResponseEntity.ok(ans);
    }

    @GetMapping("findAll")
    public ResponseEntity<List<Product>> getAllProducts()
    {
        return ResponseEntity.ok(shopService.getAllProducts());
    }

    @GetMapping("find")
    public ResponseEntity<List<Product>> getProducts(@RequestParam String query) {

        Category category = tryParseCategory(query);

        String[] keywords = query.split(" ");


        List<Product> products;
        if (category != null) {

            products = shopService.getProductsByCategory(category);
        } else {

            products = shopService.getProductsBySearch(keywords);
        }

        if (products.isEmpty()) {

            return ResponseEntity.notFound().build();
        }


        return ResponseEntity.ok(products);
    }

    private Category tryParseCategory(String query) {
        try {
            return Category.valueOf(query.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


}
