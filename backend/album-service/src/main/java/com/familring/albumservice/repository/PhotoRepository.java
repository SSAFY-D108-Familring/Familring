package com.familring.albumservice.repository;

import com.familring.albumservice.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Query("SELECT p FROM Photo p JOIN FETCH p.album WHERE p.id IN :ids")
    List<Photo> findAllByIdWithAlbum(List<Long> ids);
}
