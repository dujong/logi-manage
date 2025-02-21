package com.logi_manage.order_fulfillment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderFulfillmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderFulfillmentServiceApplication.class, args);
	}

}
