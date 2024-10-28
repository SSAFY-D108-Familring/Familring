package com.familring.userservice.config.s3;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.familring.userservice.exception.S3FileNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    public String uploadFileToS3(MultipartFile multipartFile, String filePath) throws IOException {
        // MultipartFile -> File 변환
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));

        return upload(uploadFile, filePath);
    }

    // File을 바로 S3로 업로드
    public String uploadFileToS3(File file, String filePath) throws IOException {
        return upload(file, filePath);
    }

    // S3로 업로드
    private String upload(File uploadFile, String dirName){
        // S3에 저장될 파일 이름
        String fileName = dirName + "/" + uploadFile.getName();

        String uploadImageUrl = putS3(uploadFile, fileName);
        // S3에 업로드 후 로컬 파일 삭제
        removeNewFile(uploadFile); // convert() 과정에서 로컬에 생성된 파일 삭제

        return uploadImageUrl;
    }

    // S3로 File 업로드
    private String putS3(File uploadFile, String fileName){

//        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(
//                CannedAccessControlList.PublicRead));
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile) // PublicRead 권한으로 upload
        );

        return amazonS3Client.getUrl(bucket, fileName).toString(); // File의 URL return
    }

    // S3에 있는 파일 삭제
    public void deleteS3(String filePath) throws Exception {
        try {
            URI uri = new URI(filePath); // URL에서 객체 키 추출
            String key = uri.getPath().substring(1); // URL의 첫 번째 '/'를 제거하여 객체 키 얻기

            // 파일 존재 여부 확인
            if (amazonS3Client.doesObjectExist(bucket, key)) {
                // S3에서 파일 삭제
                amazonS3Client.deleteObject(bucket, key);
                log.info("File deleted successfully: {}", key);
            } else { // file not found
                log.warn("File not found: {}", key);
                throw new S3FileNotFoundException("파일이 존재하지 않습니다.");
            }
        } catch (Exception exception) {
            log.info(exception.getMessage());
        }
        log.info("[S3 Uploader]: S3에 있는 파일 삭제");
    }

    // 로컬에 파일 업로드 및 변환(MultipartFile -> File)
    public Optional<File> convert(MultipartFile multipartFile) throws IOException{
        // 기존 파일 이름으로 새로운 File 객체 생성
        // 해당 객체는 프로그램이 실행되는 로컬 디렉토리(루트 디렉토리)에 위치하게 됨
        // 로컬에서 저장할 파일 경로: user.dir (현재 디렉토리 기준)
        String dirPath = System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename();
        File convertFile = new File(dirPath);

        if (convertFile.createNewFile()){ // 해당 경로에 파일이 없을 경우, 새 파일 생성
            // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                // multipartFile의 내용을 byte로 가져와서 write
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }

        // 새파일이 성공적으로 생성되지 않았다면, 비어있는 Optional 객체를 반환
        return Optional.empty();
    }

    // 로컬에 저장된 파일 삭제
    private void removeNewFile(File targetFile){
        String name = targetFile.getName();

        // convert() 과정에서 로컬에 생성된 파일을 삭제
        if (targetFile.delete()){
            log.info("[파일 업로드]: "+name + "로컬 파일 삭제 완료");
            return;
        }
        log.info("[파일 업로드]: "+name + "로컬 파일 삭제 실패");
    }
}
