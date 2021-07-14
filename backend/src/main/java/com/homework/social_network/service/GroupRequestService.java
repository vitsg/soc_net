package com.homework.social_network.service;

import com.homework.social_network.entity.FriendRequest;
import com.homework.social_network.entity.GroupRequest;
import com.homework.social_network.enums.GroupRelationStatus;
import com.homework.social_network.repo.GroupRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.homework.social_network.utils.Utils.getCurrentAccountId;

@Service
public class GroupRequestService {

    @Autowired
    private GroupRequestRepository groupRequestRepository;

    @Transactional
    public GroupRelationStatus joinGroup(UUID targetId) {
        UUID currentUserId = getCurrentAccountId();
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setOwnerAccountId(currentUserId);
        groupRequest.setTargetGroupId(targetId);
        groupRequest.setCreateDate(LocalDateTime.now());
        groupRequestRepository.save(groupRequest);
        return GroupRelationStatus.PARTICIPANT;
    }

    @Transactional
    public GroupRelationStatus leaveGroup(UUID targetId) {
        UUID currentUserId = getCurrentAccountId();
        GroupRequest groupRequest = groupRequestRepository.findByOwnerAccountIdAndTargetGroupId(currentUserId, targetId);
        groupRequestRepository.delete(groupRequest);
        return GroupRelationStatus.NONE;
    }

    @Transactional
    public List<UUID> getParticipantGroupIds(UUID accountId, int start, int end) {
        return groupRequestRepository.findByOwnerAccountId(accountId, start, end - start);
    }

    @Transactional
    public int countParticipantGroups(UUID accountId) {
        return groupRequestRepository.countByOwnerAccountId(accountId);
    }

    public GroupRequest getRequestGroup(UUID main, UUID target) {
        return groupRequestRepository.findByOwnerAccountIdAndTargetGroupId(main, target);
    }
}
