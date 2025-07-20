package com.example.InventoryManagment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.InventoryManagment.repository")
@EntityScan(basePackages = "com.example.InventoryManagment.models")
public class InventoryManagmentApplication {
	public static void main(String[] args) {
		SpringApplication.run(InventoryManagmentApplication.class, args);
	}
}
