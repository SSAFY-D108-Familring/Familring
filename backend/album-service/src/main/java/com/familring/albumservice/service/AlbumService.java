package com.familring.albumservice.service;

import com.familring.albumservice.domain.Album;
import com.familring.albumservice.domain.Album.AlbumBuilder;
import com.familring.albumservice.domain.AlbumType;
import com.familring.albumservice.dto.response.AlbumResponse;
import com.familring.albumservice.dto.request.AlbumRequest;
import com.familring.albumservice.dto.request.AlbumUpdateRequest;
import com.familring.albumservice.dto.response.PhotoResponse;
import com.familring.albumservice.exception.album.AlbumNotFoundException;
import com.familring.albumservice.exception.album.InvalidAlbumParameterException;
import com.familring.albumservice.exception.album.InvalidAlbumRequestException;
import com.familring.albumservice.repository.AlbumQueryRepository;
import com.familring.albumservice.repository.AlbumRepository;
import com.familring.albumservice.service.client.FamilyServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.familring.albumservice.domain.AlbumType.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AlbumService {

    private final FamilyServiceFeignClient familyServiceFeignClient;
    private final AlbumRepository albumRepository;
    private final AlbumQueryRepository albumQueryRepository;

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

    @Transactional
    public void updateAlbum(AlbumUpdateRequest albumUpdateRequest, Long albumId, Long userId) {
        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData().getFamilyId();
        Album album = albumRepository.findById(albumId).orElseThrow(AlbumNotFoundException::new);

        if (!album.getFamilyId().equals(familyId)) {
            throw new InvalidAlbumRequestException();
        }

        album.updateAlbumName(albumUpdateRequest.getAlbumName());
    }

    @Transactional
    public void deleteAlbum(Long albumId, Long userId) {
        Album album = albumRepository.findById(albumId).orElseThrow(AlbumNotFoundException::new);
        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData().getFamilyId();

        if (!album.getFamilyId().equals(familyId)) {
            throw new InvalidAlbumRequestException();
        }

        albumRepository.delete(album);
    }

    public Map<AlbumType, List<AlbumResponse>> getAlbums(List<AlbumType> albumTypes, Long userId) {
        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData().getFamilyId();
        List<Album> albums = albumQueryRepository.findByAlbumType(albumTypes, familyId);
        Map<AlbumType, List<AlbumResponse>> classifiedAlbums = new HashMap<>();

        albums.forEach(album -> {
            List<AlbumResponse> responseList = classifiedAlbums.computeIfAbsent(album.getAlbumType(), key -> new ArrayList<>());
            responseList.add(AlbumResponse.builder()
                    .id(album.getId())
                    .albumName(album.getAlbumName())
                    .thumbnailUrl(album.getPhotos().get(album.getPhotos().size() - 1).getPhotoUrl())
                    .photoCount(album.getPhotos().size())
                    .build());
        });

        return classifiedAlbums;
    }

    public List<PhotoResponse> getPhotos(Long albumId, Long userId) {
        Album album = albumRepository.findById(albumId).orElseThrow(AlbumNotFoundException::new);
        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData().getFamilyId();

        if (!album.getFamilyId().equals(familyId)) {
            throw new InvalidAlbumRequestException();
        }

        return album.getPhotos().stream().map(
                photo -> PhotoResponse.builder().id(photo.getId())
                        .photoUrl(photo.getPhotoUrl()).build()).toList();
    }
}
