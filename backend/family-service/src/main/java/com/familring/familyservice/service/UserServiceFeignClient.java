package com.familring.familyservice.service;

import com.familring.common_service.dto.BaseResponse;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserServiceFeignClient {
    @PostMapping("/users/info")
    BaseResponse<List<UserInfoResponse>> getAllUser(@RequestBody List<Long> userIds);
}
