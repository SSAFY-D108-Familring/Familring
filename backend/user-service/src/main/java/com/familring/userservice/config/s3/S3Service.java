package com.familring.userservice.config.s3;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface S3Service {
    // 멀티파트 파일 S3 업로드
    String uploadS3(MultipartFile file, String type) throws IOException;

    // 파일 S3 업로드
    String uploadS3(File file, String type) throws IOException;

    // S3 파일 삭제
    void deleteS3(String fileUrl) throws Exception;
}
