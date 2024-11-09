package com.familring.interestservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class InterestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterestServiceApplication.class, args);
    }

}

