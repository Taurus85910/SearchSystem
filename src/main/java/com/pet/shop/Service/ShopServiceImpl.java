package com.pet.shop.Service;

import com.pet.shop.Model.Category;
import com.pet.shop.Model.Product;
import com.pet.shop.Repository.ShopRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Primary
@AllArgsConstructor
public class ShopServiceImpl implements ShopService {

    ShopRepository shopRepository;

    @Override
    public List<Product> getProductsByCategory(Category category) {
        return shopRepository.findByCategory(category);
    }

    @Override
    public List<Product> getProductsBySearch(String[] keywords) {
        Set<Product> filteredProducts = new HashSet<>();

        for (String keyword : keywords) {
            List<Product> productsForKeyword = shopRepository.findBySearchKeywords(keyword);
            filteredProducts.addAll(productsForKeyword);
        }


        return filteredProducts.stream()
                .filter(product -> containsAllKeywords(product, keywords))
                .collect(Collectors.toList());

    }

    private boolean containsAllKeywords(Product product, String[] keywords) {

        for (String keyword : keywords) {
            if (!(product.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                    product.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                    product.getTags().stream().anyMatch(tag -> tag.toLowerCase().contains(keyword.toLowerCase())))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Product> getAllProducts() {
        return shopRepository.findAll();
    }

    @Override
    public Product createOrUpdate(Product product) {
        if (product.getId() != null && shopRepository.existsById(product.getId())) {
            Product existing = shopRepository.findById(product.getId()).orElseThrow();
            existing.setName(product.getName());
            existing.setDescription(product.getDescription());
            existing.setImageUrl(product.getImageUrl());
            existing.setTags(product.getTags());
            existing.setCategory(product.getCategory());
            return shopRepository.save(existing);
        } else {
            return shopRepository.save(product);
        }
    }
}
