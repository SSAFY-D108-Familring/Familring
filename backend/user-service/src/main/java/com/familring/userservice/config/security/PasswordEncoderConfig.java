package com.familring.userservice.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 비밀번호를 해싱해 BCryptPasswordEncoder를 반환하여 비밀번호를 암호화
        return new BCryptPasswordEncoder();
    }
}
