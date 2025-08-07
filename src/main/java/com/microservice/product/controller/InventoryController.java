package com.microservice.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.product.dto.InventoryDto;
import com.microservice.product.service.InventoryService;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

	@Autowired
	private InventoryService inventoryService;

	@GetMapping("/{id}")
	public ResponseEntity<InventoryDto> getInventoryById(@PathVariable Integer id) {
		InventoryDto inventory = inventoryService.getInventoryById(id);
		return ResponseEntity.ok(inventory);
	}

	@PostMapping
	public ResponseEntity<InventoryDto> createInventory(@RequestBody InventoryDto inventoryDto) {
		InventoryDto createdInventory = inventoryService.createInventory(inventoryDto);
		return ResponseEntity.ok(createdInventory);
	}

	@PutMapping("/{id}/quantity")
	public ResponseEntity<InventoryDto> updateInventoryQuantity(@PathVariable Integer id,
			@RequestParam Integer quantity) {
		InventoryDto updatedInventory = inventoryService.updateInventoryQuantity(id, quantity);
		return ResponseEntity.ok(updatedInventory);
	}
}
