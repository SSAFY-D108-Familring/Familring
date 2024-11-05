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
import org.springframework.util.CollectionUtils;
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
     * folderPath를 지정하면 해당 폴더 아래에 파일이 저장됩니다.
     *
     * @param files      업로드할 파일 리스트
     * @param folderPath S3에 저장될 폴더 경로 (선택사항)
     * @return 업로드된 파일들의 URL 리스트
     * @throws S3Exception 파일 업로드 중 오류 발생 시
     */
    public List<String> uploadFiles(List<MultipartFile> files, String folderPath) {
        if (CollectionUtils.isEmpty(files)) {
            throw S3Exception.noFilesToUpload();
        }

        List<String> uploadedUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                log.warn("빈 파일이 포함되어 있습니다. 건너뜁니다.");
                continue;
            }

            try {
                uploadedUrls.add(upload(file, folderPath));
            } catch (IOException e) {
                log.error("파일 업로드 중 오류 발생: {}", e.getMessage(), e);
                throw S3Exception.fileUploadError();
            }
        }

        if (uploadedUrls.isEmpty()) {
            throw S3Exception.noFilesUploaded();
        }

        return uploadedUrls;
    }

    /**
     * S3에서 파일들을 삭제합니다.
     *
     * @param fileUrls 삭제할 파일 URL 리스트
     * @throws S3Exception 파일 삭제 중 오류 발생 시
     */
    public void deleteFiles(List<String> fileUrls) {
        if (CollectionUtils.isEmpty(fileUrls)) {
            throw S3Exception.noUrlsToDelete();
        }

        for (String fileUrl : fileUrls) {
            if (fileUrl == null || fileUrl.trim().isEmpty()) {
                log.warn("잘못된 파일 URL이 포함되어 있습니다. 건너뜁니다.");
                continue;
            }

            delete(fileUrl.trim());
        }
    }

    /**
     * 실제 파일 업로드를 처리하는 private 메서드
     */
    private String upload(MultipartFile file, String folderPath) throws IOException {
        String fileName = createFileName(file.getOriginalFilename());
        String fileKey = createFileKey(folderPath, fileName);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3Client.putObject(new PutObjectRequest(bucket, fileKey, file.getInputStream(), metadata));
        return amazonS3Client.getUrl(bucket, fileKey).toString();
    }

    /**
     * 실제 파일 삭제를 처리하는 private 메서드
     */
    private void delete(String fileUrl) {
        String fileKey = extractFileKey(fileUrl);
        log.info("삭제할 파일 키: {}", fileKey);

        if (!amazonS3Client.doesObjectExist(bucket, fileKey)) {
            throw S3Exception.fileNotFound(fileKey);
        }

        try {
            amazonS3Client.deleteObject(bucket, fileKey);
            log.info("파일 삭제 완료: {}", fileKey);
        } catch (Exception e) {
            log.error("파일 삭제 중 오류 발생: {}", e.getMessage(), e);
            throw S3Exception.fileDeleteError();
        }
    }

    /**
     * 파일명 생성 (UUID + 원본 파일명)
     */
    private String createFileName(String originalFileName) {
        // 특수문자를 _로 변환하고 연속된 _를 하나로 합침
        String sanitizedFileName = originalFileName
                .replaceAll("[^a-zA-Z0-9._-]", "_")  // 특수문자를 _로 변환
                .replaceAll("_+", "_");              // 연속된 _를 하나로 합침
        return UUID.randomUUID().toString() + "_" + sanitizedFileName;
    }

    /**
     * 폴더 경로와 파일명을 조합하여 최종 파일 키를 생성
     */
    private String createFileKey(String folderPath, String fileName) {
        if (folderPath == null || folderPath.trim().isEmpty()) {
            return fileName;
        }

        String normalizedPath = folderPath.trim();
        normalizedPath = normalizedPath.replaceAll("^/+|/+$", "");

        return normalizedPath + "/" + fileName;
    }

    /**
     * URL에서 파일 키 추출
     */
    private String extractFileKey(String fileUrl) {
        String[] urlParts = fileUrl.split(bucket + ".s3");
        if (urlParts.length < 2) {
            throw S3Exception.invalidFileUrl();
        }
        return urlParts[1].substring(urlParts[1].indexOf("/") + 1);
    }
}