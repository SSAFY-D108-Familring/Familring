package com.familring.userservice.service.client;

import com.familring.common_module.dto.BaseResponse;
import com.familring.userservice.model.dto.request.PersonAlbumUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("album-service")
public interface AlbumServiceFeignClient {

    @PutMapping("/client/albums")
    BaseResponse<Void> updatePersonAlbumName(@RequestBody PersonAlbumUpdateRequest personAlbumUpdateRequest);

    @DeleteMapping("/client/albums")
    BaseResponse<Void> deletePersonAlbum(@RequestParam Long userId);
}

