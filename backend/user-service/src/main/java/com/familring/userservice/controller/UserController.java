package com.familring.userservice.controller;

import com.familring.userservice.model.dto.request.UserJoinRequest;
import com.familring.userservice.model.dto.request.UserLoginRequest;
import com.familring.userservice.model.dto.response.JwtTokenResponse;
import com.familring.userservice.service.CustomUserDetailsService;
import com.familring.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "회원 컨트롤러", description = "회원관련 기능 수행")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "카카오톡 소셜 로그인", description = "로그인 시 회원가입 여부 확인 후 회원가입")
    public ResponseEntity login(@RequestBody UserLoginRequest userLogInRequest) {
        JwtTokenResponse tokens = userService.login(userLogInRequest);

        return ResponseEntity.ok(tokens);
    }

    @PostMapping(value = "/join", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "회원가입", description = "회원가입이 되어 있지 않은 사용자 회원가입 처리 후 JWT 발급")
    public ResponseEntity join
            (@RequestPart("userJoinRequest") UserJoinRequest userLogInRequest,
             @RequestPart(value = "image", required = false) MultipartFile image) {
        JwtTokenResponse tokens = userService.join(userLogInRequest, image);

        return ResponseEntity.ok(tokens);
    }


}
