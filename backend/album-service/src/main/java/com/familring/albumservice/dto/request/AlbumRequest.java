package com.familring.albumservice.dto.request;

import com.familring.albumservice.domain.AlbumType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlbumRequest {
    private Long scheduleId;
    private String albumName;
    private AlbumType albumType;
}
