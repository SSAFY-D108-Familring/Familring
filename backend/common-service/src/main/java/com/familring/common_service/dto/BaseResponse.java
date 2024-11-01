package com.familring.common_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    private int statusCode;
    private String message;
    private T data;

    public static <T> BaseResponse<T> create(int statusCode, String message) {
        return new BaseResponse<>(statusCode, message, null);
    }

    public static <T> BaseResponse<T> create(int statusCode, String message, T dto) {
        return new BaseResponse<>(statusCode, message, dto);
    }
}
