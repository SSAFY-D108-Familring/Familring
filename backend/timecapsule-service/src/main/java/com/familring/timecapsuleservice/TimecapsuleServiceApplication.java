package com.familring.timecapsuleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TimecapsuleServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimecapsuleServiceApplication.class, args);
    }

}
