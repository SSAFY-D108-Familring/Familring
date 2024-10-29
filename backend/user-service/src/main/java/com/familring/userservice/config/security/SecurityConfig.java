package com.familring.userservice.config.security;

import com.familring.userservice.config.jwt.JwtAuthenticationFilter;
import com.familring.userservice.config.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import java.util.function.Supplier;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${familring.server.url}")
    private String serverUrl;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable) // 기본 인증을 비활성화 (HTTP Basic Authentication)
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호를 비활성화 (Cross-Site Request Forgery)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 관리를 Stateless로 설정 (서버에 세션을 저장하지 않음)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/actuator/**").permitAll()
                                .requestMatchers("/**").access(this::hasIpAddresses)
                                .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가

        return http.build(); // 설정된 HttpSecurity 객체를 빌드하여 반환
    }

    private AuthorizationDecision hasIpAddresses(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        HttpServletRequest request = context.getRequest();
        boolean isValidIp = Stream.of(serverUrl, "127.0.0.1")
                .map(IpAddressMatcher::new)
                .anyMatch(matcher -> matcher.matches(request));

        return new AuthorizationDecision(isValidIp);
    }
}
