package com.vin.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vin.inventory.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	Optional<Inventory> findBySkuCode(String skuCode);
	
	//for a given skuCode it'll give us a list of inventory items!
	List<Inventory> findBySkuCodeIn(List<String> skuCode);

}
