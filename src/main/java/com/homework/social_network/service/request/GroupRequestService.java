package com.homework.social_network.service.request;

import com.homework.social_network.entity.GroupJoin;
import com.homework.social_network.enums.ErrorCode;
import com.homework.social_network.enums.GroupRelationStatus;
import com.homework.social_network.exception.CustomException;
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
    public GroupRelationStatus joinGroup(UUID currentAccountId, UUID targetGroupId, GroupRelationStatus status) {
        if (groupRequestRepository.findByOwnerAccountIdAndTargetGroupId(currentAccountId, targetGroupId) != null) {
            throw new CustomException(ErrorCode.GROUP_REQUEST_ALREADY_EXISTS);
        }
        GroupJoin groupJoin = new GroupJoin();
        groupJoin.setOwnerAccountId(currentAccountId);
        groupJoin.setTargetGroupId(targetGroupId);
        groupJoin.setCreateDate(LocalDateTime.now());
        groupJoin.setStatus(status);
        groupRequestRepository.save(groupJoin);
        return GroupRelationStatus.PARTICIPANT;
    }

    @Transactional
    public GroupRelationStatus leaveGroup(UUID targetId) {
        UUID currentUserId = getCurrentAccountId();
        GroupJoin groupJoin = groupRequestRepository.findByOwnerAccountIdAndTargetGroupId(currentUserId, targetId);
        if (groupJoin == null) {
            throw new CustomException(ErrorCode.GROUP_REQUEST_DOES_NOT_EXISTS);
        }
        groupRequestRepository.delete(groupJoin);
        return GroupRelationStatus.NONE;
    }

    public GroupJoin getRequestGroup(UUID main, UUID target) {
        return groupRequestRepository.findByOwnerAccountIdAndTargetGroupId(main, target);
    }

    public List<GroupJoin> getRequestGroups(UUID main, List<UUID> targetIds) {
        return groupRequestRepository.findByOwnerAccountIdAndTargetGroupIdIn(main, targetIds);
    }
}
