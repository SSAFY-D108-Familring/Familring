package com.familring.fileservice.exception;

import com.familring.fileservice.exception.base.ApplicationException;
import com.familring.fileservice.exception.constant.ErrorDetail;

public class S3Exception extends ApplicationException {
    public S3Exception(ErrorDetail errorDetail) {
        super(errorDetail);
    }

    // 자주 사용되는 예외들에 대한 팩토리 메서드
    public static S3Exception fileUploadError() {
        return new S3Exception(ErrorDetail.S3_FILE_UPLOAD_ERROR);
    }

    public static S3Exception fileDeleteError() {
        return new S3Exception(ErrorDetail.S3_FILE_DELETE_ERROR);
    }

    public static S3Exception fileNotFound(String fileKey) {
        return new S3Exception(ErrorDetail.S3_FILE_NOT_FOUND);
    }

    public static S3Exception noFilesToUpload() {
        return new S3Exception(ErrorDetail.S3_NO_FILES_TO_UPLOAD);
    }

    public static S3Exception noFilesUploaded() {
        return new S3Exception(ErrorDetail.S3_NO_FILES_UPLOADED);
    }

    public static S3Exception invalidFileUrl() {
        return new S3Exception(ErrorDetail.S3_INVALID_FILE_URL);
    }
}