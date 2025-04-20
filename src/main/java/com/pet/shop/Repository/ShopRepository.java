package com.pet.shop.Repository;

import com.pet.shop.Model.Category;
import com.pet.shop.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ShopRepository extends JpaRepository<Product, Long> {

    boolean existsById(Long id);
    List<Product> findByCategory(Category category);
    @Query("SELECT p FROM Product p JOIN p.tags t WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :keywords, '%')) OR " +
            "LOWER(t) LIKE LOWER(CONCAT('%', :keywords, '%'))")
    List<Product> findBySearchKeywords(@Param("keywords") String keywords);
    List<Product> findAll();
}
