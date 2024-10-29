package com.familring.fileservice.dto;

// DTO 클래스
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
public class FileDeleteRequest {
    private List<String> fileUrls;
}