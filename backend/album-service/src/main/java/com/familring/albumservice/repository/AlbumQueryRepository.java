package com.familring.albumservice.repository;

import com.familring.albumservice.domain.Album;
import com.familring.albumservice.domain.AlbumType;
import com.familring.albumservice.dto.AlbumResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AlbumQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

//    public List<Album> findAlbumsByAlbumType(List<AlbumType> albumTypes) {
//        List<Album> albums = jpaQueryFactory
//                .select()
//    }
}
