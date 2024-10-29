package com.familring.userservice.config.security;

import com.familring.userservice.config.jwt.JwtAuthenticationFilter;
import com.familring.userservice.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(httpConfig -> httpConfig.disable()) // 기본 인증을 비활성화 (HTTP Basic Authentication)
                .csrf(csrfConfig -> csrfConfig.disable()) // CSRF 보호를 비활성화 (Cross-Site Request Forgery)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 관리를 Stateless로 설정 (서버에 세션을 저장하지 않음)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**").permitAll() // Swagger UI 접근 허용
                        .requestMatchers("/v3/api-docs/**").permitAll() // API 문서 접근 허용
                        .requestMatchers("/users/join/**", "/users/login/**").permitAll() // 회원가입, 로그인, 토큰 재발급 접근 허용
                        .requestMatchers("/test/**").permitAll() // 테스트 접근 허용
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가

        return http.build(); // 설정된 HttpSecurity 객체를 빌드하여 반환
    }

}
