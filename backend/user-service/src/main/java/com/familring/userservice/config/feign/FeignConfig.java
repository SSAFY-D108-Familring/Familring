package com.familring.userservice.config.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import feign.Client;
import feign.httpclient.ApacheHttpClient;

@Configuration
public class FeignConfig {
    @Bean
    public Client feignClient() {
        return new ApacheHttpClient();
    }
}
