package com.microservice.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductProducerService {

	private static final String TOPIC = "product-notification";

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void sendProductEvent(String message) {
		kafkaTemplate.send(TOPIC, message);
	}
}
