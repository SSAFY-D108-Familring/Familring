package com.familring.familyservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableMongoRepositories(basePackages = "com.familring.familyservice.model.repository")
@EnableAsync
@MapperScan("com.familring.familyservice.model.dao")
public class FamilyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FamilyServiceApplication.class, args);
	}

}

