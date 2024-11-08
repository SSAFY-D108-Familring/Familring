package com.familring.albumservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AlbumInfoResponse {
    private Long id;
    private String albumName;
    private String thumbnailUrl;
    private int photoCount;
}

