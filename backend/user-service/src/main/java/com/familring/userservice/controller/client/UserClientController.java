package com.familring.userservice.controller.client;

import com.familring.common_module.dto.BaseResponse;
import com.familring.userservice.model.dto.response.UserInfoResponse;
import com.familring.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client/users")
@RequiredArgsConstructor
@Log4j2
@Hidden
public class UserClientController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<BaseResponse<List<UserInfoResponse>>> getAllUser(@RequestBody List<Long> userIds) {
        log.info("userIds: {}", userIds);
        List<UserInfoResponse> response = userService.getAllUser(userIds);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "회원 정보를 모두 성공적으로 조회 했습니다.", response));
    }
}
