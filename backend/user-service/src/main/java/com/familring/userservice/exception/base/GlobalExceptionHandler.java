package com.familring.userservice.exception.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        ErrorResponse errorResponse = new ErrorResponse(message);
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorResponse);
    }

    @ExceptionHandler(PatternSyntaxException.class)
    public ResponseEntity<ErrorResponse> handlePatternSyntaxException(PatternSyntaxException ex) {
        String message = "올바른 패턴을 입력하세요. ";
        ErrorResponse errorResponse = new ErrorResponse(message);
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        String message = "클라이언트가 사용한 HTTP 메서드가 리소스에서 허용되지 않습니다.";
        ErrorResponse errorResponse = new ErrorResponse(message);
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED.value()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        String message = "서버에서 요청을 처리하는 동안 오류가 발생했습니다.";
        ErrorResponse errorResponse = new ErrorResponse(message);
        loggingException(ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
    }

    private void loggingException(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String stackTraceString = sw.toString();
        log.error("Stack trace: {}", stackTraceString);
    }
}