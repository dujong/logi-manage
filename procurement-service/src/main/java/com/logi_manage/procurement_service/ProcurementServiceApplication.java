package com.logi_manage.procurement_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProcurementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProcurementServiceApplication.class, args);
	}

}
