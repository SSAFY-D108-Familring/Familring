package com.familring.familyservice.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonAlbumCreateRequest {
    private Long userId;
    private Long familyId;
}
