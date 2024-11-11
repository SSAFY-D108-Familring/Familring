package com.familring.albumservice.service.client;

import com.familring.albumservice.dto.client.UserInfoResponse;
import com.familring.common_module.dto.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-service")
public interface UserServiceFeignClient {
    @GetMapping("/client/users/{userId}")
    BaseResponse<UserInfoResponse> getUser(@PathVariable Long userId);
}