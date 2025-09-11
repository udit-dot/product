package com.microservice.product.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservice.product.dto.InventoryDto;
import com.microservice.product.dto.ProductDto;
import com.microservice.product.entity.Inventory;
import com.microservice.product.entity.Product;
import com.microservice.product.repo.InventoryRepository;
import com.microservice.product.repo.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ProductProducerService productProducerService;

	public ProductDto getProductById(Integer productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

		// Map entity to DTO using ModelMapper
		ProductDto productDto =  modelMapper.map(product, ProductDto.class);
		// Send product data as notification to Kafka
	    try {
	    	productProducerService.sendProductEvent("Product data sent : " +productDto);
	    } catch (Exception e) {
	        // Log error but don't block the main flow
	        e.printStackTrace();
	    }

	    return productDto;
	}

	public ProductDto createProduct(ProductDto productDto) {
		// Create a new Product entity without inventory to avoid transient object
		// issues
		Product product = new Product();
		product.setProductName(productDto.getProductName());
		product.setDescription(productDto.getDescription());
		product.setPrice(productDto.getPrice());
		product.setCategory(productDto.getCategory());

		// Save product first
		Product savedProduct = productRepository.save(product);

		// Create default inventory for the product if inventory is provided in DTO
		if (productDto.getInventory() != null) {
			InventoryDto inventoryDto = productDto.getInventory();
			inventoryDto.setProductId(savedProduct.getProductId());

			// Create inventory entity
			Inventory inventory = new Inventory();
			inventory.setQuantity(inventoryDto.getQuantity());
			inventory.setLocation(inventoryDto.getLocation());
			inventory.setProduct(savedProduct);

			// Set default status based on quantity
			if (inventory.getQuantity() == null || inventory.getQuantity() <= 0) {
				inventory.setStatus("OUT_OF_STOCK");
			} else if (inventory.getQuantity() <= 10) {
				inventory.setStatus("LOW_STOCK");
			} else {
				inventory.setStatus("AVAILABLE");
			}

			// Save inventory
			Inventory savedInventory = inventoryRepository.save(inventory);

			// Update the product with the saved inventory
			savedProduct.setInventory(savedInventory);
		}

		return modelMapper.map(savedProduct, ProductDto.class);
	}
}
