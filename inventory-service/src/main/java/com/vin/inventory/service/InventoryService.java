package com.vin.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vin.inventory.dto.InventoryRes;
import com.vin.inventory.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Generates constructor for final fields
public class InventoryService {

	private final InventoryRepository inventoryRepo;

	// Read-only transaction to fetch stock status for given skuCodes
	@Transactional(readOnly = true)
	public List<InventoryRes> isInStock(List<String> skuCode) {
		// Fetch inventory entries matching the skuCodes and map to response DTO
		return inventoryRepo.findBySkuCodeIn(skuCode)
			.stream()
			.map(inventory ->
				InventoryRes.builder()
					.skuCode(inventory.getSkuCode())
					.isInStock(inventory.getQuantity() > 0) // true if quantity > 0
					.build())
			.toList();
	}
}
