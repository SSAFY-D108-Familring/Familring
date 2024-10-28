package com.familring.userservice.config.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Log4j2
public class S3ServiceImpl implements S3Service {

    private final S3Uploader s3Uploader;

    @Override
    public String uploadS3(MultipartFile file, String type) {
        return "";
    }

    @Override
    public String uploadS3(File file, String type) {
        return "";
    }

    @Override
    public void deleteS3(String fileUrl) {

    }
}
