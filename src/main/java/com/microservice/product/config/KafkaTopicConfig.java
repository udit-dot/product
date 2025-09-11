package com.microservice.product.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

	@Bean
	public NewTopic productNotificationTopic() {
		return new NewTopic("product-notification", 1, (short) 1);
	}

	@Bean
	public NewTopic inventoryNotificationTopic() {
		return new NewTopic("inventory-notification", 1, (short) 1);
	}
}
