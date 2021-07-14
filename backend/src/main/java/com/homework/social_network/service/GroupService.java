package com.homework.social_network.service;

import com.homework.social_network.dto.request.GroupCreateRequestDto;
import com.homework.social_network.dto.request.GroupUpdateRequestDto;
import com.homework.social_network.dto.response.group.GroupPageInfoAccessResponseDto;
import com.homework.social_network.dto.response.group.GroupPageInfoResponseDto;
import com.homework.social_network.dto.response.group.GroupPageMetainfoResponseDto;
import com.homework.social_network.dto.response.group.GroupWithPhotoResponseDto;
import com.homework.social_network.dto.response.group.GroupsResponseDto;
import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.BaseEntity;
import com.homework.social_network.entity.Group;
import com.homework.social_network.entity.Photo;
import com.homework.social_network.enums.GroupRelationStatus;
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
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.homework.social_network.utils.Utils.getCurrentAccount;
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

    @Autowired
    private GroupRequestService groupRequestService;

    @Autowired
    private GroupRelationService groupRelationService;

    @Transactional
    public GroupWithPhotoResponseDto create(GroupCreateRequestDto groupCreateRequestDto) {
        Account currentAccount = getCurrentAccount();
        Group group = modelMapper.map(groupCreateRequestDto, Group.class);
        group.setCreateDate(LocalDateTime.now());
        group.setOwner(currentAccount);
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
        Account currentAccount = getCurrentAccount();
        GroupPageInfoResponseDto groupPageInfoResponseDto = new GroupPageInfoResponseDto();
        GroupWithPhotoResponseDto dto = modelMapper.map(group, GroupWithPhotoResponseDto.class);
        dto.setPhoto(getOrDefaultContentPhoto(group.getPhoto()));
        groupPageInfoResponseDto.setGroup(dto);

        GroupPageInfoAccessResponseDto accesses = new GroupPageInfoAccessResponseDto();
        accesses.setCanModify(currentAccount.getId().equals(group.getOwner().getId()));
        groupPageInfoResponseDto.setAccesses(accesses);

        GroupPageMetainfoResponseDto metainfo = new GroupPageMetainfoResponseDto();
        metainfo.setGroupRelationStatus(groupRelationService.getRelationGroup(currentAccount, group));
        groupPageInfoResponseDto.setInfo(metainfo);
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

    @Transactional
    public Map<UUID, GroupWithPhotoResponseDto> getBatch(List<UUID> ids) {
        return groupRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(BaseEntity::getId, g -> {
                    GroupWithPhotoResponseDto dto = modelMapper.map(g, GroupWithPhotoResponseDto.class);
                    dto.setPhoto(getOrDefaultContentPhoto(g.getPhoto()));
                    return dto;
                }));
    }

    @Transactional
    public GroupRelationStatus joinGroup(UUID targetId) {
        return groupRequestService.joinGroup(targetId);
    }

    @Transactional
    public GroupRelationStatus leaveGroup(UUID targetId) {
        return groupRequestService.leaveGroup(targetId);
    }

    @Transactional
    public GroupsResponseDto getParticipantGroups(UUID accountId, int start, int end) {
        List<UUID> groupIds = groupRequestService.getParticipantGroupIds(accountId, start, end);
        Map<UUID, GroupWithPhotoResponseDto> batch = getBatch(groupIds);
        List<GroupWithPhotoResponseDto> groups = groupIds.stream()
                .map(batch::get)
                .collect(Collectors.toList());
        GroupsResponseDto groupsResponseDto = new GroupsResponseDto();
        groupsResponseDto.setGroups(groups);
        groupsResponseDto.setTotalSize(groupRequestService.countParticipantGroups(accountId));
        return groupsResponseDto;
    }
}
