package com.familring.albumservice.service;

import com.familring.albumservice.domain.Album;
import com.familring.albumservice.domain.Album.AlbumBuilder;
import com.familring.albumservice.domain.AlbumType;
import com.familring.albumservice.domain.Photo;
import com.familring.albumservice.dto.response.AlbumInfoResponse;
import com.familring.albumservice.dto.request.AlbumRequest;
import com.familring.albumservice.dto.request.AlbumUpdateRequest;
import com.familring.albumservice.dto.response.AlbumResponse;
import com.familring.albumservice.dto.response.PhotoItem;
import com.familring.albumservice.exception.album.AlbumNotFoundException;
import com.familring.albumservice.exception.album.InvalidAlbumParameterException;
import com.familring.albumservice.exception.album.InvalidAlbumRequestException;
import com.familring.albumservice.repository.AlbumQueryRepository;
import com.familring.albumservice.repository.AlbumRepository;
import com.familring.albumservice.repository.PhotoRepository;
import com.familring.albumservice.service.client.FamilyServiceFeignClient;
import com.familring.albumservice.service.client.FileServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.familring.albumservice.domain.AlbumType.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log4j2
public class AlbumService {

    private final FamilyServiceFeignClient familyServiceFeignClient;
    private final FileServiceFeignClient fileServiceFeignClient;
    private final AlbumRepository albumRepository;
    private final AlbumQueryRepository albumQueryRepository;
    private final PhotoRepository photoRepository;

    @Value("${aws.s3.album-photo-path}")
    private String albumPhotoPath;

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
            log.error(albumRequest.getScheduleId());
            if (userId == null || albumRequest.getScheduleId() != null) {
                throw new InvalidAlbumParameterException();
            }
            albumBuilder.userId(userId);
        } else if (albumRequest.getAlbumType() == SCHEDULE) {
            if (albumRequest.getScheduleId() == null) {
                throw new InvalidAlbumParameterException();
            }
            albumBuilder.scheduleId(albumRequest.getScheduleId());
        } else if (albumRequest.getAlbumType() == NORMAL) {
            if (albumRequest.getScheduleId() != null) {
                throw new InvalidAlbumParameterException();
            }
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
        fileServiceFeignClient.deleteFiles(album.getPhotos().stream().map(Photo::getPhotoUrl).toList());
    }

    public Map<AlbumType, List<AlbumInfoResponse>> getAlbums(List<AlbumType> albumTypes, Long userId) {
        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData().getFamilyId();
        List<Album> albums = albumQueryRepository.findByAlbumType(albumTypes, familyId);
        Map<AlbumType, List<AlbumInfoResponse>> classifiedAlbums = new HashMap<>();

        albums.forEach(album -> {
            List<AlbumInfoResponse> responseList = classifiedAlbums.computeIfAbsent(album.getAlbumType(), key -> new ArrayList<>());
            responseList.add(AlbumInfoResponse.builder()
                    .id(album.getId())
                    .albumName(album.getAlbumName())
                    .thumbnailUrl(album.getPhotos().isEmpty() ?
                            null : album.getPhotos().get(album.getPhotos().size() - 1).getPhotoUrl())
                    .photoCount(album.getPhotos().size())
                    .build());
        });

        return classifiedAlbums;
    }

    public AlbumResponse getPhotos(Long albumId, Long userId) {
        Album album = albumRepository.findById(albumId).orElseThrow(AlbumNotFoundException::new);
        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData().getFamilyId();

        if (!album.getFamilyId().equals(familyId)) {
            throw new InvalidAlbumRequestException();
        }

        return AlbumResponse.builder().albumName(album.getAlbumName()).photos(album.getPhotos().stream().map(
                photo -> PhotoItem.builder().id(photo.getId()).photoUrl(photo.getPhotoUrl()).build()).toList()).build();
    }

    @Transactional
    public void addPhotos(Long albumId, List<MultipartFile> photos, Long userId) {
        Album album = albumRepository.findById(albumId).orElseThrow(AlbumNotFoundException::new);
        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData().getFamilyId();

        if (!album.getFamilyId().equals(familyId)) {
            throw new InvalidAlbumRequestException();
        }

        // 가족 구성원 불러오기

        // 얼사분

        // 가족

        List<String> photoUrls = fileServiceFeignClient.uploadFiles(photos, getAlbumPhotoPath(familyId)).getData();
        List<Photo> newPhotos = photoUrls.stream().map(url -> Photo.builder().photoUrl(url).build()).toList();
        album.addPhotos(newPhotos);
    }

    @Transactional
    public void deletePhotos(List<Long> photoIds, Long albumId, Long userId) {
        List<Photo> photos = photoRepository.findAllByIdWithAlbum(photoIds);
        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData().getFamilyId();
        photos.forEach(photo -> {
            if (!photo.getAlbum().getFamilyId().equals(familyId) || !photo.getAlbum().getId().equals(albumId)) {
                throw new InvalidAlbumRequestException();
            }
        });
        // DB에서 삭제
        photoRepository.deleteAll(photos);
        // AWS S3에서 삭제
        fileServiceFeignClient.deleteFiles(photos.stream().map(Photo::getPhotoUrl).toList());
    }

    private String getAlbumPhotoPath(Long familyId) {
        return albumPhotoPath + "/" + familyId;
    }

    public Album getAlbumByScheduleId(Long scheduleId) {
        return albumRepository.findByScheduleId(scheduleId).orElse(null);
    }
}
