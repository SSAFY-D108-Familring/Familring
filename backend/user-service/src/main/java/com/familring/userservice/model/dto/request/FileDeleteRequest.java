package com.familring.userservice.model.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
public class FileDeleteRequest {
    private List<String> fileUrls;
}