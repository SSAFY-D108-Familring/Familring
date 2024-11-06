package com.familring.albumservice.repository;

import com.familring.albumservice.domain.Album;
import com.familring.albumservice.domain.AlbumType;
import com.familring.albumservice.domain.QAlbum;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlbumQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QAlbum album = QAlbum.album;

    public List<Album> findByAlbumType(List<AlbumType> albumTypes, Long familyId) {
        return jpaQueryFactory
                .select(album)
                .from(album)
                .where(albumTypeIn(albumTypes), familyIdEq(familyId))
                .orderBy(album.id.desc())
                .fetch();
    }

    private BooleanExpression familyIdEq(Long familyId) {
        if (familyId == null) return null;
        return album.familyId.eq(familyId);
    }

    private BooleanExpression albumTypeIn(List<AlbumType> albumTypes) {
        if (albumTypes == null || albumTypes.isEmpty()) {
            return null;
        }
        return album.albumType.in(albumTypes);
    }
}
