package com.familring.fileservice.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.familring.fileservice.exception.S3Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class FileService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 파일들을 S3에 업로드하고 URL 리스트를 반환합니다.
     * 단일 파일이나 다중 파일 모두 처리 가능합니다.
     */
    public List<String> uploadFiles(List<MultipartFile> files) {
        List<String> uploadedUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    uploadedUrls.add(upload(file));
                } catch (IOException e) {
                    throw new S3Exception(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드 중 오류가 발생했습니다.");
                }
            }
        }

        return uploadedUrls;
    }

    /**
     * S3에서 파일들을 삭제합니다.
     * 단일 URL이나 다중 URL 모두 처리 가능합니다.
     */
    public void deleteFiles(List<String> fileUrls) {
        for (String fileUrl : fileUrls) {
            if (fileUrl != null && !fileUrl.isEmpty()) {
                delete(fileUrl);
            }
        }
    }

    /**
     * 실제 파일 업로드를 처리하는 private 메서드
     */
    private String upload(MultipartFile file) throws IOException {
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    /**
     * 실제 파일 삭제를 처리하는 private 메서드
     */
    private void delete(String fileUrl) {
        String fileName = extractFileName(fileUrl);
        log.info("삭제할 파일 이름: {}", fileName);
        if (!amazonS3Client.doesObjectExist(bucket, fileName)) {
            throw new S3Exception(HttpStatus.NOT_FOUND,  String.format("존재하지 않는 파일입니다. 파일명: %s", fileName));
        }

        try {
            amazonS3Client.deleteObject(bucket, fileName);
        } catch (Exception e) {
            throw new S3Exception(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제 중 오류가 발생했습니다.");
        }
    }

    /**
     * 파일명 생성 (UUID + 원본 파일명)
     */
    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }

    /**
     * URL에서 파일명 추출
     */
    private String extractFileName(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }
}