package com.familring.userservice.config.security;

import com.familring.userservice.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AuthConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // AuthenticationManager를 authenticationConfiguration에서 가져와 반환
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        // DaoAuthenticationProvider는 스프링 시큐리티에서 데이터베이스 기반 인증을 처리하는 클래스
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // 사용자 인증 정보를 CustomUserDetailsService에서 로드하도록 설정
        authProvider.setUserDetailsService(customUserDetailsService);
        // 패스워드 암호화를 위해 BCryptPasswordEncoder를 설정
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());

        return authProvider;
    }
}

