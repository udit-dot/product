package com.microservice.product.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.product.dto.InventoryDto;
import com.microservice.product.entity.Inventory;
import com.microservice.product.repo.InventoryRepository;

@Service
public class InventoryService {
	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private InventoryProducerService inventoryProducerService;

	public InventoryDto getInventoryById(Integer inventoryId) {
		Inventory inventory = inventoryRepository.findById(inventoryId)
				.orElseThrow(() -> new RuntimeException("Inventory not found with id: " + inventoryId));

		// Map entity to DTO using ModelMapper
		return modelMapper.map(inventory, InventoryDto.class);
	}
	
	public InventoryDto createInventory(InventoryDto inventoryDto) {
		// Map DTO to entity using ModelMapper
		Inventory inventory = modelMapper.map(inventoryDto, Inventory.class);

		Inventory savedInventory = inventoryRepository.save(inventory);

		// Map saved entity back to DTO
		return modelMapper.map(savedInventory, InventoryDto.class);
	}
	
	public InventoryDto updateInventoryQuantity(Integer inventoryId, Integer newQuantity) {
		Inventory inventory = inventoryRepository.findById(inventoryId)
				.orElseThrow(() -> new RuntimeException("Inventory not found with id: " + inventoryId));

		inventory.setQuantity(newQuantity);

		// Update status based on quantity
		if (newQuantity <= 0) {
			inventory.setStatus("OUT_OF_STOCK");
		} else if (newQuantity <= 10) {
			inventory.setStatus("LOW_STOCK");
		} else {
			inventory.setStatus("AVAILABLE");
		}

		Inventory updatedInventory = inventoryRepository.save(inventory);

		// Map updated entity to DTO
		InventoryDto updatedDto = modelMapper.map(updatedInventory, InventoryDto.class);
		
		// Send Kafka notification
	    try {
	        inventoryProducerService.sendInventoryEvent("Inventory Data updated : " + updatedDto);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return updatedDto;
	}
}
