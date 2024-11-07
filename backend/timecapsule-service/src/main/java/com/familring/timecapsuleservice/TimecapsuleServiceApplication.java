package com.familring.timecapsuleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.familring.timecapsuleservice.service.client")
public class TimecapsuleServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimecapsuleServiceApplication.class, args);
    }

}

