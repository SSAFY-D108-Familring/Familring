package com.familring.albumservice.service;

import com.familring.albumservice.domain.Album;
import com.familring.albumservice.domain.Album.AlbumBuilder;
import com.familring.albumservice.domain.AlbumType;
import com.familring.albumservice.dto.request.AlbumRequest;
import com.familring.albumservice.exception.album.InvalidAlbumParameterException;
import com.familring.albumservice.repository.AlbumRepository;
import com.familring.albumservice.service.client.FamilyServiceFeignClient;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.familring.albumservice.domain.AlbumType.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlbumService {

    private final FamilyServiceFeignClient familyServiceFeignClient;
    private final AlbumRepository albumRepository;

    /**
     * 일반 앨범 - UserId, ScheduleId가 null
     * 인물 앨범 - UserId만 있음
     * 일정 앨범 - ScheduleId만 있음
     */
    @Transactional
    public void createAlbum(AlbumRequest albumRequest, Long userId) {
        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData().getFamilyId();

        AlbumBuilder albumBuilder = Album.builder().familyId(familyId).albumName(albumRequest.getAlbumName())
                .albumType(albumRequest.getAlbumType());

        if (albumRequest.getAlbumType() == PERSON) {
            if (userId == null) {
                throw new InvalidAlbumParameterException();
            }
            albumBuilder.userId(userId);
        } else if (albumRequest.getAlbumType() == SCHEDULE) {
            if (albumRequest.getScheduleId() == null) {
                throw new InvalidAlbumParameterException();
            }
            albumBuilder.scheduleId(albumRequest.getScheduleId());
        }

        Album album = albumBuilder.build();
        albumRepository.save(album);
    }
}
