package com.familring.apigateway.filter;

import com.familring.apigateway.exception.JwtValidationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Component
@Log4j2
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Value("${jwt.secret-key}")
    private String secretKey;
    private SecretKey key;

    public AuthorizationHeaderFilter() {
        super(Config.class);
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer", "").strip();

            try {
                Long userId = validateTokenAndGetUserId(jwt);

                // 새로운 헤더를 추가한 요청 객체 생성
                ServerHttpRequest newRequest = request.mutate()
                        .header("X-User-ID", String.valueOf(userId))
                        .build();

                // 수정된 요청으로 교체한 새로운 ServerWebExchange 생성
                ServerWebExchange newExchange = exchange.mutate()
                        .request(newRequest)
                        .build();

                return chain.filter(newExchange);  // 새로운 exchange로 다음 필터 호출
            } catch (JwtValidationException e) {
                return onError(exchange, e.getMessage(), e.getStatus());
            }
        });
    }

    private Long validateTokenAndGetUserId(String jwt) {
        try {
            var claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();

            String subject = claims.getSubject();
            if (subject == null || subject.isEmpty()) {
                throw new JwtValidationException("JWT subject is missing", HttpStatus.UNAUTHORIZED);
            }

            // userId 클레임 가져오기
            Long userId = claims.get("userId", Long.class);
            if (userId == null) {
                throw new JwtValidationException("User ID claim is missing", HttpStatus.UNAUTHORIZED);
            }

            return userId;  // userId 반환

        } catch (ExpiredJwtException e) {
            throw new JwtValidationException("JWT token has expired", HttpStatus.UNAUTHORIZED);
        } catch (SecurityException | MalformedJwtException e) {
            throw new JwtValidationException("Invalid JWT signature", HttpStatus.UNAUTHORIZED);
        } catch (UnsupportedJwtException e) {
            throw new JwtValidationException("Unsupported JWT token", HttpStatus.UNAUTHORIZED);
        } catch (IllegalArgumentException e) {
            throw new JwtValidationException("JWT claims string is empty", HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(err);
        return response.setComplete();
    }

    @PostConstruct
    public void generateKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
}