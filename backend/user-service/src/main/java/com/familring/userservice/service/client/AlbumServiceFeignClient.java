package com.familring.userservice.service.client;

import com.familring.common_module.dto.BaseResponse;
import com.familring.userservice.model.dto.request.PersonAlbumUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("album-service")
public interface AlbumServiceFeignClient {

    @PutMapping("/client/albums")
    BaseResponse<Void> updatePersonAlbumName(@RequestBody PersonAlbumUpdateRequest personAlbumUpdateRequest);
}