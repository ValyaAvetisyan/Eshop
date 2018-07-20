package com.example.eshop.repository;

import com.example.eshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findOneById(int id);
}
