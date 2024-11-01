package com.familring.userservice.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "file-service")
public interface FileServiceFeignClient {
    @PostMapping(value = "/client/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<List<String>> uploadFiles(
            @RequestPart("files") List<MultipartFile> files,
            @RequestParam(value = "folderPath", required = false) String folderPath
    );

    @DeleteMapping("/client/files")
    ResponseEntity<Void> deleteFiles(@RequestBody List<String> fileUrls);
}
