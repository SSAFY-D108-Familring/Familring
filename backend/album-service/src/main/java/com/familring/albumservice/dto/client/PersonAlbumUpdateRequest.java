package com.familring.albumservice.dto.client;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PersonAlbumUpdateRequest {
    private Long userId;
    private String userNickname;
}
