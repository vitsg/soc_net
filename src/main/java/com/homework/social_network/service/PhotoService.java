package com.homework.social_network.service;

import com.homework.social_network.dto.response.PhotoResponseDto;
import com.homework.social_network.dto.response.PhotosResponseDto;
import com.homework.social_network.entity.Photo;
import com.homework.social_network.repo.PhotoRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PhotoService {

    @Autowired private PhotoRepository photoRepository;
    @Autowired private ModelMapper modelMapper;

    @Transactional
    public void addPhotos(UUID id, List<MultipartFile> photos) {
        if (photos == null) {
            log.info("List of photos is null");
            return;
        }
        log.info("Photos size is {}", photos.size());
        photos.forEach(p -> {
            try {
                Photo photo = new Photo();
                photo.setOwnerId(id);
                photo.setMain(false);
                photo.setData(p.getBytes());
                photo.setCreateDate(LocalDateTime.now());
                photoRepository.save(photo);
            } catch (Exception e) {
                log.error("Error by set photo", e);
                throw new RuntimeException("Error by set photo", e);
            }
        });
    }

    @Transactional
    public PhotosResponseDto getPhotos(UUID id, int start, int end) {
        List<PhotoResponseDto> photos = photoRepository.getByOwnerIdAndMainIsFalse(id, start, end - start)
                .stream()
                .map(p -> modelMapper.map(p, PhotoResponseDto.class))
                .collect(Collectors.toList());
        PhotosResponseDto photosResponseDto = new PhotosResponseDto();
        photosResponseDto.setPhotos(photos);
        photosResponseDto.setTotalSize(photoRepository.countByOwnerIdAndMainIsFalse(id));
        return photosResponseDto;
    }

    @Transactional
    public void removePhotos(List<UUID> ids) {
        photoRepository.deleteAllByIdIn(ids);
    }
}
