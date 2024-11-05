package com.familring.albumservice.controller;

import com.familring.albumservice.dto.request.AlbumRequest;
import com.familring.albumservice.service.AlbumService;
import com.familring.common_module.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/albums")
@RequiredArgsConstructor
@Tag(name = "앨범 컨트롤러", description = "앨범 관리 기능을 수행합니다.")
public class AlbumController {

    private final AlbumService albumService;

    @PostMapping
    @Operation(summary = "앨범 생성", description = "일반 앨범, 일정 앨범, 얼굴 사진 분류를 위한 앨범을 생성합니다.")
    public ResponseEntity<BaseResponse<Void>> createAlbum(
            @RequestBody AlbumRequest albumRequest,
            @Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        albumService.createAlbum(albumRequest, userId);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "앨범이 생성되었습니다."));
    }
}
