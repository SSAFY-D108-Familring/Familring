package com.familring.calendarservice.service.client;

import com.familring.common_module.dto.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "album-service")
public interface AlbumServiceFeignClient {
    @GetMapping("/client/albums")
    BaseResponse<Long> getAlbumIdByScheduleId(@RequestParam Long scheduleId);
}
