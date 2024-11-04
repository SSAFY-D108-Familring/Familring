package com.familring.timecapsuleservice.service.client;

import com.familring.common_module.dto.BaseResponse;
import com.familring.timecapsuleservice.dto.client.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserServiceFeignClient {
    @PostMapping("/client/users")
    BaseResponse<List<UserInfoResponse>> getAllUser(@RequestBody List<Long> userIds);
}
