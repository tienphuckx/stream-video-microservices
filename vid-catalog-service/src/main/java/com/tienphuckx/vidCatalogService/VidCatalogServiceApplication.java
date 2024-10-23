package com.tienphuckx.vidCatalogService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VidCatalogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VidCatalogServiceApplication.class, args);
	}

}
