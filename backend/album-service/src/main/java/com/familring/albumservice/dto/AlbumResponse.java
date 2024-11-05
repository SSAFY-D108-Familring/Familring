package com.familring.albumservice.dto;

import com.familring.albumservice.domain.AlbumType;
import com.familring.albumservice.domain.Photo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AlbumResponse {
    private Long id;
    private String albumName;
    private String thumbnailUrl;
    private int photoCount;
}

