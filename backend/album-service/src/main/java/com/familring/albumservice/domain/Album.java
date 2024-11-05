package com.familring.albumservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Long id;

    @Column(name = "family_id")
    private Long familyId;

    @Column(name = "user_id", nullable = true)
    private Long userId;

    @Column(name = "schedule_id", nullable = true)
    private Long scheduleId;

    @Column(name = "album_name")
    private String albumName;

    @Column(name = "album_type")
    @Enumerated(EnumType.STRING)
    private AlbumType albumType;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Photo> photos = new ArrayList<>();
}
