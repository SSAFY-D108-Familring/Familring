package com.familring.albumservice.dto.client;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class FaceSimilarityResponse {
    private String imageUrl;

    private Map<Long, Double> similarities;

    private int faceCount;
}