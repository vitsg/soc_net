package com.homework.social_network.service;

import com.homework.social_network.dto.request.GroupCreateRequestDto;
import com.homework.social_network.dto.request.GroupUpdateRequestDto;
import com.homework.social_network.dto.response.GroupPageInfoResponseDto;
import com.homework.social_network.dto.response.GroupWithPhotoResponseDto;
import com.homework.social_network.dto.response.GroupsResponseDto;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.homework.social_network.utils.Utils.getOrDefaultContentPhoto;

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
    public GroupWithPhotoResponseDto create(GroupCreateRequestDto groupCreateRequestDto) {
        Group group = modelMapper.map(groupCreateRequestDto, Group.class);
        group.setCreateDate(LocalDateTime.now());
        Group savedGroup = groupRepository.save(group);
        setPhoto(savedGroup, groupCreateRequestDto.getPhoto());
        GroupWithPhotoResponseDto dto = modelMapper.map(savedGroup, GroupWithPhotoResponseDto.class);
        dto.setPhoto(getOrDefaultContentPhoto(savedGroup.getPhoto()));
        return dto;
    }

    @Transactional
    public GroupWithPhotoResponseDto findOne(String id) {
        Group group = groupRepository.getByIdWithPhoto(UUID.fromString(id));
        GroupWithPhotoResponseDto dto = modelMapper.map(group, GroupWithPhotoResponseDto.class);
        dto.setPhoto(getOrDefaultContentPhoto(group.getPhoto()));
        return dto;
    }

    @Transactional
    public GroupWithPhotoResponseDto update(GroupUpdateRequestDto groupUpdateRequestDto) {
        Group group = groupRepository.getByIdWithPhoto(groupUpdateRequestDto.getId());
        modelMapper.map(groupUpdateRequestDto, group);
        setPhoto(group, groupUpdateRequestDto.getPhoto());
        Group savedGroup = groupRepository.save(group);
        GroupWithPhotoResponseDto dto = modelMapper.map(savedGroup, GroupWithPhotoResponseDto.class);
        dto.setPhoto(getOrDefaultContentPhoto(savedGroup.getPhoto()));
        return dto;
    }

    @Transactional
    public GroupPageInfoResponseDto getPageInfo(UUID id) {
        Group group = groupRepository.getByIdWithPhoto(id);
        return createPageInfo(group);
    }

    private GroupPageInfoResponseDto createPageInfo(Group group) {
        GroupPageInfoResponseDto groupPageInfoResponseDto = new GroupPageInfoResponseDto();
        GroupWithPhotoResponseDto dto = modelMapper.map(group, GroupWithPhotoResponseDto.class);
        dto.setPhoto(getOrDefaultContentPhoto(group.getPhoto()));
        groupPageInfoResponseDto.setGroup(dto);
        return groupPageInfoResponseDto;
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

    @Transactional
    public GroupsResponseDto getAllGroups(int start, int end) {
        List<GroupWithPhotoResponseDto> groups = groupRepository.getAllGroups(start, end - start)
                .stream()
                .map(group -> {
                    GroupWithPhotoResponseDto dto = modelMapper.map(group, GroupWithPhotoResponseDto.class);
                    dto.setPhoto(getOrDefaultContentPhoto(group.getPhoto()));
                    return dto;
                })
                .collect(Collectors.toList());
        GroupsResponseDto groupsResponseDto = new GroupsResponseDto();
        groupsResponseDto.setGroups(groups);
        groupsResponseDto.setTotalSize((int) groupRepository.count());
        return groupsResponseDto;
    }
}
