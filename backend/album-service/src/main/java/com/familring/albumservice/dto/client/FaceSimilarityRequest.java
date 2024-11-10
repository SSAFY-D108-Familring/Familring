package com.familring.albumservice.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FaceSimilarityRequest {
    private List<String> targetImages;

    private List<Person> people;
}