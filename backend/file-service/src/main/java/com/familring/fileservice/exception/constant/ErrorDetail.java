package com.familring.fileservice.exception.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorDetail {
    S3_FILE_UPLOAD_ERROR("S3001", HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 중 오류가 발생했습니다."),
    S3_FILE_DELETE_ERROR("S3002", HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제 중 오류가 발생했습니다."),
    S3_FILE_NOT_FOUND("S3003", HttpStatus.NOT_FOUND, "존재하지 않는 파일입니다."),
    S3_NO_FILES_TO_UPLOAD("S3004", HttpStatus.BAD_REQUEST, "업로드할 파일이 없습니다."),
    S3_NO_FILES_UPLOADED("S3005", HttpStatus.BAD_REQUEST, "업로드된 파일이 없습니다."),
    S3_INVALID_FILE_URL("S3006", HttpStatus.BAD_REQUEST, "잘못된 파일 URL 형식입니다.");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
}