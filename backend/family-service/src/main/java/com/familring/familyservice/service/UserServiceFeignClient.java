package com.familring.familyservice.service;

import com.familring.familyservice.model.dto.response.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserServiceFeignClient {
    @GetMapping
    UserInfoResponse getUserInfo(@RequestParam("userId") Long userId);
}
