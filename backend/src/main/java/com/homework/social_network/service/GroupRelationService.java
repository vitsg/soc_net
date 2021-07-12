package com.homework.social_network.service;

import com.homework.social_network.dto.response.GroupWithPhotoResponseDto;
import com.homework.social_network.dto.response.GroupsResponseDto;
import com.homework.social_network.enums.GroupRelationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GroupRelationService {

    @Autowired
    private GroupRequestService groupRequestService;

    @Autowired
    private GroupService groupService;

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
        Map<UUID, GroupWithPhotoResponseDto> batch = groupService.getBatch(groupIds);
        List<GroupWithPhotoResponseDto> groups = groupIds.stream()
                .map(batch::get)
                .collect(Collectors.toList());
        GroupsResponseDto groupsResponseDto = new GroupsResponseDto();
        groupsResponseDto.setGroups(groups);
        groupsResponseDto.setTotalSize(groupRequestService.countParticipantGroups(accountId));
        return groupsResponseDto;
    }
}
