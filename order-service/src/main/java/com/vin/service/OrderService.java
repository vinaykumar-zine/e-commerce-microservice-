package com.vin.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.vin.dto.InventoryRes;
import com.vin.dto.OrderRequest;
import com.vin.entity.Order;
import com.vin.entity.OrderLineItems;
import com.vin.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service  // Marks this class as a Spring service component
@Transactional  // Makes all public methods transactional (committed/rolled back atomically)
@AllArgsConstructor  // Lombok: generates constructor for all final fields
public class OrderService {

	private final ModelMapper modelMapper;        // Used to convert DTOs to entity classes
	private final OrderRepository orderRepo;      // Spring Data repository for Order persistence
	private final WebClient.Builder webClientBuilder;            // Reactive HTTP client to call Inventory microservice

	/**
	 * Places an order if all products are in stock by:
	 * - Generating an order number
	 * - Mapping incoming DTOs to entity
	 * - Checking inventory via a REST call
	 * - Saving the order if all items are available
	 */
	public String placeOrder(OrderRequest orderReq) {

		// Create a new order and assign a random UUID as the order number
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());

		// Convert OrderLineItemsDto list to OrderLineItems entities using ModelMapper
		List<OrderLineItems> orderLineItems = orderReq.getOrderLineItemsDto()
			.stream()
			.map(oli -> modelMapper.map(oli, OrderLineItems.class))
			.toList();

		// Set the list of order items into the Order entity
		order.setOrderLineItems(orderLineItems);

		// Extract SKU codes from the orderLineItems to check with Inventory Service
		List<String> skuCodes = orderLineItems.stream()
			.map(OrderLineItems::getSkuCode)
			.toList();

		//Make GET request to Inventory Service with all skuCodes as query params
		//In this way we are not sending multiple api calls for each skuCode, we are just sending only one api call!
		InventoryRes[] inventoryResponses = webClientBuilder.build().get()
		        .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
				        .retrieve()
				        .bodyToMono(InventoryRes[].class)	// Expecting an array of InventoryRes as response
				        .block();		// Blocking call to wait for response (OK here since this is not reactive end-to-end)

		//Check if all products in the response are marked "in stock"
		boolean allProductsInStock = Arrays.stream(inventoryResponses)
			.allMatch(InventoryRes::isInStock);
		
		
		if (allProductsInStock) {
            orderRepo.save(order);
            return "Order Placed";
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
	}
}
