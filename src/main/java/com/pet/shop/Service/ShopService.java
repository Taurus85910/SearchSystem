package com.pet.shop.Service;


import com.pet.shop.Model.Category;
import com.pet.shop.Model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShopService {

    List<Product> getProductsByCategory(Category category);
    List<Product> getProductsBySearch(String[] keywords);
    List<Product> getAllProducts();
    Product createOrUpdate(Product product);

}
