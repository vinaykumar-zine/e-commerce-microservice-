package com.vin.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.vin.dto.ProductRequest;
import com.vin.dto.ProductResponse;
import com.vin.entities.Product;
import com.vin.repository.ProductRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Service  // Marks this class as a Spring-managed service component
@AllArgsConstructor  // Lombok: generates constructor for all final fields
@Slf4j  // Lombok: adds a logger instance (log)
public class ProductService {
	
	private final ProductRepository productRepository;  // Handles DB operations for Product entity
	private final ModelMapper modelMapper;  // Maps between DTOs and entity classes

	/**
	 * Creates and saves a product entity using the incoming DTO.
	 * Logs the ID of the saved product.
	 */
	public String createProduct(ProductRequest productReq) {
		Product product = Product.builder()
				.name(productReq.getName())
				.description(productReq.getDescription())
				.price(productReq.getPrice())
				.build();
		
		productRepository.save(product);  // Save product to database
		log.info("Product {} is saved", product.getId());  // Log saved product ID
		return "Product created successfully!";
	}

	/**
	 * Fetches all products from DB and maps them to DTOs for client response.
	 */
	public List<ProductResponse> getAllProducts() {
	    List<Product> products = productRepository.findAll();  // Retrieve all products
	    return products.stream()
	                   .map(prod -> modelMapper.map(prod, ProductResponse.class))  // Map to response DTO
	                   .toList();  // Return list of response DTOs
	}
}