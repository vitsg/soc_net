package com.homework.social_network.service;

import com.homework.social_network.dto.request.GroupCreateRequestDto;
import com.homework.social_network.dto.request.GroupUpdateRequestDto;
import com.homework.social_network.dto.response.group.access.GroupAccessResponseDto;
import com.homework.social_network.dto.response.group.list.GroupEntryResponseDto;
import com.homework.social_network.dto.response.group.list.GroupSimpleResponseDto;
import com.homework.social_network.dto.response.group.page.GroupPageInfoResponseDto;
import com.homework.social_network.dto.response.group.info.GroupMetainfoResponseDto;
import com.homework.social_network.dto.response.group.GroupWithPhotoPageResponseDto;
import com.homework.social_network.dto.response.group.list.GroupsResponseDto;
import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.BaseEntity;
import com.homework.social_network.entity.Group;
import com.homework.social_network.entity.Photo;
import com.homework.social_network.enums.GroupRelationStatus;
import com.homework.social_network.repo.GroupRepository;
import com.homework.social_network.repo.PhotoRepository;
import com.homework.social_network.service.relation.GroupRelationService;
import com.homework.social_network.service.request.GroupRequestService;
import com.homework.social_network.specification.group.AllGroupSpecificationHepler;
import com.homework.social_network.utils.CustomPageable;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.homework.social_network.utils.Utils.getCurrentAccount;
import static com.homework.social_network.utils.Utils.getOrDefaultContentPhoto;
import static com.homework.social_network.utils.Utils.validatePhotoSize;

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
    public GroupWithPhotoPageResponseDto create(GroupCreateRequestDto groupCreateRequestDto) {
        Account currentAccount = getCurrentAccount();
        Group group = modelMapper.map(groupCreateRequestDto, Group.class);
        group.setCreateDate(LocalDateTime.now());
        group.setOwner(currentAccount);
        Group savedGroup = groupRepository.save(group);
        setPhoto(savedGroup, groupCreateRequestDto.getPhoto());
        groupRequestService.joinGroup(currentAccount.getId(), savedGroup.getId(), GroupRelationStatus.OWNER);
        GroupWithPhotoPageResponseDto dto = modelMapper.map(savedGroup, GroupWithPhotoPageResponseDto.class);
        dto.setPhoto(getOrDefaultContentPhoto(savedGroup.getPhoto()));
        return dto;
    }

    @Transactional
    public GroupWithPhotoPageResponseDto findOne(String id) {
        Group group = groupRepository.getByIdWithPhoto(UUID.fromString(id));
        GroupWithPhotoPageResponseDto dto = modelMapper.map(group, GroupWithPhotoPageResponseDto.class);
        dto.setPhoto(getOrDefaultContentPhoto(group.getPhoto()));
        return dto;
    }

    @Transactional
    public Group getGroup(UUID id) {
        return groupRepository.getById(id);
    }

    @Transactional
    public GroupWithPhotoPageResponseDto update(GroupUpdateRequestDto groupUpdateRequestDto) {
        Group group = groupRepository.getByIdWithPhoto(groupUpdateRequestDto.getId());
        modelMapper.map(groupUpdateRequestDto, group);
        setPhoto(group, groupUpdateRequestDto.getPhoto());
        Group savedGroup = groupRepository.save(group);
        GroupWithPhotoPageResponseDto dto = modelMapper.map(savedGroup, GroupWithPhotoPageResponseDto.class);
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
        GroupWithPhotoPageResponseDto dto = modelMapper.map(group, GroupWithPhotoPageResponseDto.class);
        dto.setPhoto(getOrDefaultContentPhoto(group.getPhoto()));
        groupPageInfoResponseDto.setGroup(dto);

        GroupAccessResponseDto accesses = new GroupAccessResponseDto();
        accesses.setCanModify(currentAccount.getId().equals(group.getOwner().getId()));
        groupPageInfoResponseDto.setAccesses(accesses);

        GroupMetainfoResponseDto metainfo = new GroupMetainfoResponseDto();
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
        validatePhotoSize(photoFile);
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
        Photo forRemove = group.getPhoto();
        Optional.ofNullable(forRemove).ifPresent(p -> photoRepository.delete(p));
        group.setPhoto(null);
    }

    @Transactional
    public GroupsResponseDto getAllGroups(int start, int end, String name) {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = new CustomPageable(start, end - start, sort);
        Account currentAccount = getCurrentAccount();
        List<GroupEntryResponseDto> groups = groupRepository.findAll(AllGroupSpecificationHepler.allGroupSpecification(name), pageable)
                .stream()
                .map(group -> {
                    GroupSimpleResponseDto simple = modelMapper.map(group, GroupSimpleResponseDto.class);
                    simple.setPhoto(getOrDefaultContentPhoto(group.getPhoto()));
                    GroupMetainfoResponseDto info = GroupMetainfoResponseDto.builder()
                            .groupRelationStatus(groupRelationService.getRelationGroup(currentAccount, group))
                            .build();
                    return GroupEntryResponseDto.builder()
                            .group(simple)
                            .info(info)
                            .build();
                })
                .collect(Collectors.toList());
        return GroupsResponseDto.builder()
                .groups(groups)
                .totalSize((int) groupRepository.count(AllGroupSpecificationHepler.countAllGroupSpecification(name)))
                .build();
    }

    @Transactional
    public Map<UUID, Group> getBatch(List<UUID> ids) {
        return groupRepository.findAllByIds(ids)
                .stream()
                .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));
    }

}
