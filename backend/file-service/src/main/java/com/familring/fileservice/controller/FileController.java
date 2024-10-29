package com.familring.fileservice.controller;

import com.familring.fileservice.dto.FileDeleteRequest;
import com.familring.fileservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 파일 업로드 처리
     * 단일 또는 다중 파일 업로드를 모두 처리합니다.
     * MultipartFile은 RequestBody로 받을 수 없어 @RequestParam을 유지합니다.
     * @param files 업로드할 파일 리스트
     * @return 업로드된 파일들의 URL 리스트
     */
    @PostMapping("/")
    public ResponseEntity<List<String>> uploadFiles(
            @RequestParam("files") List<MultipartFile> files) {
        List<String> uploadedUrls = fileService.uploadFiles(files);
        return ResponseEntity.ok(uploadedUrls);
    }

    /**
     * 파일 삭제 처리
     * 단일 또는 다중 파일 삭제를 모두 처리합니다.
     * @param request 삭제할 파일 URL 리스트를 포함한 요청 객체
     * @return void
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteFiles(
            @RequestBody FileDeleteRequest request) {
        fileService.deleteFiles(request.getFileUrls());
        return ResponseEntity.ok().build();
    }
}
