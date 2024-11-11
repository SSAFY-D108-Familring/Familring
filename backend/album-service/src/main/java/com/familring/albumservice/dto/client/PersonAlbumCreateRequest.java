package com.familring.albumservice.dto.client;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PersonAlbumCreateRequest {
    private Long userId;
    private Long familyId;
}
