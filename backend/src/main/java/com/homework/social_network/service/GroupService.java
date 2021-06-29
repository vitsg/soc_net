package com.homework.social_network.service;

import com.homework.social_network.dto.request.GroupUpdateRequestDto;
import com.homework.social_network.dto.request.GroupCreateRequestDto;
import com.homework.social_network.dto.response.GroupPageInfoResponseDto;
import com.homework.social_network.dto.response.GroupResponseDto;
import com.homework.social_network.entity.Group;
import com.homework.social_network.entity.Photo;
import com.homework.social_network.repo.GroupRepository;
import com.homework.social_network.repo.PhotoRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.homework.social_network.utils.Utils.getDefaultImage;

@Service
@Slf4j
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public GroupResponseDto create(GroupCreateRequestDto groupCreateRequestDto) {
        Group group = modelMapper.map(groupCreateRequestDto, Group.class);
        Group savedGroup = groupRepository.save(group);
        setPhoto(savedGroup, groupCreateRequestDto.getPhoto());
        return modelMapper.map(savedGroup, GroupResponseDto.class);
    }

    @Transactional
    public GroupResponseDto findOne(String id) {
        Group group = groupRepository.getById(UUID.fromString(id));
        return modelMapper.map(group, GroupResponseDto.class);
    }

    @Transactional
    public GroupResponseDto update(GroupUpdateRequestDto groupUpdateRequestDto) {
        Group group = groupRepository.getById(groupUpdateRequestDto.getId());
        modelMapper.map(groupUpdateRequestDto, group);
        setPhoto(group, groupUpdateRequestDto.getPhoto());
        return modelMapper.map(groupRepository.save(group), GroupResponseDto.class);
    }

    @Transactional
    public byte[] getGroupPhoto(UUID id) {
        Photo photo = photoRepository.getByOwnerIdAndMainIsTrue(id);
        if (photo == null) {
            return getDefaultImage();
        } else {
            return photo.getData();
        }
    }

    @Transactional
    public GroupPageInfoResponseDto getPageInfo(UUID id) {
        Group group = groupRepository.getById(id);
        return createPageInfo(group);
    }

    private void setPhoto(Group group, MultipartFile photoFile) {
        if (photoFile == null || photoFile.getSize() == 0) {
            removePhoto(group);
        } else {
            addOrChangePhoto(photoFile, group);
        }
    }

    private void addOrChangePhoto(MultipartFile photoFile, Group group) {
        try {
            Photo forRemove = group.getPhoto();
            Optional.ofNullable(forRemove).ifPresent(p -> photoRepository.delete(p));
            Photo photo = new Photo();
            photo.setOwnerId(group.getId());
            photo.setMain(true);
            photo.setData(photoFile.getBytes());
            photo.setCreateDate(LocalDateTime.now());
            group.setPhoto(photo);
        } catch (Exception e) {
            log.error("Error by set photo", e);
            throw new RuntimeException("Error by set photo", e);
        }
    }

    private void removePhoto(Group group) {
        group.setPhoto(null);
        Photo forRemove = group.getPhoto();
        Optional.ofNullable(forRemove).ifPresent(p -> photoRepository.delete(p));
    }

    private GroupPageInfoResponseDto createPageInfo(Group group) {
        GroupPageInfoResponseDto groupPageInfoResponseDto = new GroupPageInfoResponseDto();
        groupPageInfoResponseDto.setGroup(modelMapper.map(group, GroupResponseDto.class));
        return groupPageInfoResponseDto;
    }
}
