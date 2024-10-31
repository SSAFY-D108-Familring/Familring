package com.familring.apigateway.exception;

import com.familring.common_service.base.ErrorCodeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@Order(-1)  // 높은 우선순위로 설정
@Log4j2
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Error occurred: ", ex);

        var response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ErrorCodeResponse errorResponse;
        HttpStatus status;

        if (ex instanceof JwtValidationException jwtEx) {
            status = jwtEx.getHttpStatus();
            errorResponse = new ErrorCodeResponse(
                    jwtEx.getCode(),
                    jwtEx.getMessage()
            );
        } else {
            // 기타 예외 처리
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            errorResponse = new ErrorCodeResponse(
                    "INTERNAL_SERVER_ERROR",
                    "서버 내부 오류가 발생했습니다"
            );
        }

        response.setStatusCode(status);

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (Exception e) {
            log.error("Error writing response", e);
            return Mono.error(e);
        }
    }
}