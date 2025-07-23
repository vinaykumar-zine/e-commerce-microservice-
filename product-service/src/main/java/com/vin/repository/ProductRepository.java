package com.vin.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vin.entities.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
