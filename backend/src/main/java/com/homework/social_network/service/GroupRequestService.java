package com.homework.social_network.service;

import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.Group;
import com.homework.social_network.entity.GroupJoin;
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
    public GroupRelationStatus joinGroup(Account account, Group target, GroupRelationStatus status) {
        GroupJoin groupJoin = new GroupJoin();
        groupJoin.setOwnerAccount(account);
        groupJoin.setTargetGroup(target);
        groupJoin.setCreateDate(LocalDateTime.now());
        groupJoin.setStatus(status);
        groupRequestRepository.save(groupJoin);
        return GroupRelationStatus.PARTICIPANT;
    }

    @Transactional
    public GroupRelationStatus leaveGroup(UUID targetId) {
        UUID currentUserId = getCurrentAccountId();
        GroupJoin groupJoin = groupRequestRepository.findByOwnerAccountIdAndTargetGroupId(currentUserId, targetId);
        groupRequestRepository.delete(groupJoin);
        return GroupRelationStatus.NONE;
    }

    @Transactional
    public List<GroupJoin> getAccountGroupIds(UUID accountId, int start, int end) {
        return groupRequestRepository.findByOwnerAccountId(accountId, start, end - start);
    }

    @Transactional
    public List<GroupJoin> getGroupAccountIds(UUID groupId, int start, int end) {
        return groupRequestRepository.findByTargetGroupId(groupId, start, end - start);
    }

    @Transactional
    public int countAccountGroups(UUID accountId) {
        return groupRequestRepository.countByOwnerAccountId(accountId);
    }

    @Transactional
    public int countGroupAccounts(UUID accountId) {
        return groupRequestRepository.countByTargetGroupId(accountId);
    }

    public GroupJoin getRequestGroup(UUID main, UUID target) {
        return groupRequestRepository.findByOwnerAccountIdAndTargetGroupId(main, target);
    }
}
