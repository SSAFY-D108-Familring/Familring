package com.familring.userservice.config.security;

import com.familring.userservice.config.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
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

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Log4j2
public class SecurityConfig {

    @Value("${familring.server.ip-address}")
    private String serverIpAddress;
    private final JwtTokenProvider jwtTokenProvider;
    private final DiscoveryClient discoveryClient;  // Eureka Client 주입

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/actuator/**").permitAll()
                                .requestMatchers("/**").access(this::hasIpAddresses)
                                .anyRequest().authenticated());
        return http.build();
    }

    private AuthorizationDecision hasIpAddresses(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        HttpServletRequest request = context.getRequest();
        String remoteHost = request.getRemoteHost();
        String remoteAddr = request.getRemoteAddr();

        // 호스트네임 체크
        boolean isValidHost = StringUtils.equals(serverIpAddress, remoteHost);

        // IP 주소 체크 (localhost)
        boolean isLocalhost = StringUtils.equals("127.0.0.1", remoteAddr);

        // Gateway IP 주소 체크
        List<ServiceInstance> gatewayInstances = discoveryClient.getInstances("API-GATEWAY");

        boolean isGatewayIp = gatewayInstances.stream()
                .map(ServiceInstance::getHost)
                .anyMatch(host -> StringUtils.equals(host, remoteAddr));

        gatewayInstances.stream()
                .map(ServiceInstance::getHost)
                .forEach(h -> log.info("gateway: {}", h));
        log.info("ip address: {}", remoteAddr);

        return new AuthorizationDecision(isValidHost || isGatewayIp || isLocalhost);
    }
}