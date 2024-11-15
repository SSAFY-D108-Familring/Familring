package com.familring.albumservice.repository;

import com.familring.albumservice.domain.Album;
import com.familring.albumservice.domain.AlbumType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    Optional<Album> findByScheduleId(Long scheduleId);

    @EntityGraph(attributePaths = "photos")
    List<Album> findByUserIdIn(List<Long> userIds);

    Optional<Album> findByUserIdAndAlbumType(Long userId, AlbumType albumType);

    void deleteByUserId(Long userId);
}
