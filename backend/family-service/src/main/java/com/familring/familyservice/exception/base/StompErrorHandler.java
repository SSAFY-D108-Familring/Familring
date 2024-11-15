package com.familring.familyservice.exception.base;

import com.familring.common_module.dto.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@Log4j2
@RequiredArgsConstructor
@Component
public class StompErrorHandler extends StompSubProtocolErrorHandler {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        String destination = "/queue/errors";
        ErrorResponse errorResponse;

        try {
            if (ex instanceof ApplicationException) {
                ApplicationException applicationException = (ApplicationException) ex;
                log.error("ApplicationException occurred: {}", applicationException.getMessage());
                errorResponse = new ErrorResponse(applicationException.getErrorCode(), applicationException.getMessage());
            } else {
                log.error("Unexpected exception occurred", ex);
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                String stackTrace = sw.toString();
                log.error("Stack trace: {}", stackTrace);

                errorResponse = new ErrorResponse(
                        "500",
                        "서버에서 요청을 처리하는 동안 오류가 발생했습니다."
                );
            }

            // ErrorResponse를 JSON 문자열로 변환
            String errorMessage = objectMapper.writeValueAsString(errorResponse);

            // 클라이언트에게 에러 메시지 전송
            messagingTemplate.convertAndSend(destination, errorMessage);

        } catch (JsonProcessingException e) {
            log.error("Error while processing error message", e);
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }
}