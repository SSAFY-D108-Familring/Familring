package com.familring.albumservice.controller.client;

import com.familring.albumservice.domain.Album;
import com.familring.albumservice.dto.client.PersonAlbumCreateRequest;
import com.familring.albumservice.dto.client.PersonAlbumUpdateRequest;
import com.familring.albumservice.service.AlbumService;
import com.familring.common_module.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/albums")
@RequiredArgsConstructor
@Log4j2
@Hidden
public class AlbumClientController {

    private final AlbumService albumService;

    @GetMapping
    public ResponseEntity<BaseResponse<Long>> getAlbumIdByScheduleId(@RequestParam Long scheduleId) {
        Album album = albumService.getAlbumByScheduleId(scheduleId);
        Long albumId = album == null ? null : album.getId();
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "일정 ID로 앨범 ID를 조회했습니다.", albumId));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> createPersonAlbum(@RequestBody PersonAlbumCreateRequest request) {
        albumService.createPersonAlbum(request);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "인물 앨범을 생성했습니다."));
    }

    @PutMapping
    public ResponseEntity<BaseResponse<Void>> updatePersonAlbumName(@RequestBody PersonAlbumUpdateRequest request) {
        albumService.updatePersonAlbum(request);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "인물 앨범 이름을 수정했습니다."));
    }

    @DeleteMapping
    public ResponseEntity<BaseResponse<Void>> deletePersonAlbum(@RequestParam Long userId) {
        albumService.deletePersonAlbum(userId);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "인물 앨범을 삭제했습니다."));
    }
}
