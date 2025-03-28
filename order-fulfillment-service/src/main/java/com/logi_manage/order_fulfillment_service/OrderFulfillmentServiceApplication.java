package com.logi_manage.order_fulfillment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing
public class OrderFulfillmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderFulfillmentServiceApplication.class, args);
	}

}
