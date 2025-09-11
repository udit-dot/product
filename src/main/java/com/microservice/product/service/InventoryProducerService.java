package com.microservice.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryProducerService {
	private static final String TOPIC = "inventory-notification";

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void sendInventoryEvent(String message) {
		kafkaTemplate.send(TOPIC, message);
	}
}
