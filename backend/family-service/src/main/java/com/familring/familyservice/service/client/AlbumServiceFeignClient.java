package com.familring.familyservice.service.client;

import com.familring.common_module.dto.BaseResponse;
import com.familring.familyservice.model.dto.request.PersonAlbumCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("album-service")
public interface AlbumServiceFeignClient {
    @PostMapping("/client/albums")
    BaseResponse<Void> createPersonAlbum(@RequestBody PersonAlbumCreateRequest personAlbumCreateRequest);
}

