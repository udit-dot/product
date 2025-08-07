package com.microservice.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
