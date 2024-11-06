package com.familring.albumservice.controller.client;

import com.familring.albumservice.domain.Album;
import com.familring.albumservice.service.AlbumService;
import com.familring.common_module.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client/albums")
@RequiredArgsConstructor
@Log4j2
@Hidden
public class AlbumClientController {

    private final AlbumService albumService;

    @GetMapping
    public ResponseEntity<BaseResponse<Album>> getAlbumIdByScheduleId(@RequestParam Long scheduleId) {
        Album album = albumService.getAlbumByScheduleId(scheduleId);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "일정 ID로 앨범 ID를 조회했습니다.", album));
    }
}
