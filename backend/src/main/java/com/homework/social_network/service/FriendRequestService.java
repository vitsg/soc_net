package com.homework.social_network.service;

import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.FriendRequest;
import com.homework.social_network.enums.FriendRelationStatus;
import com.homework.social_network.repo.FriendRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.homework.social_network.utils.Utils.getCurrentAccount;
import static com.homework.social_network.utils.Utils.getCurrentAccountId;

@Service
public class FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Transactional
    public FriendRelationStatus addFriendRequest(Account targetAccount) {
        Account currentAccount = getCurrentAccount();
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setOwnerAccount(currentAccount);
        friendRequest.setTargetAccount(targetAccount);
        friendRequest.setCreateDate(LocalDateTime.now());
        friendRequestRepository.save(friendRequest);
        FriendRequest inputRequest = friendRequestRepository.findByOwnerAccountIdAndTargetAccountId(targetAccount.getId(), currentAccount.getId());
        return inputRequest == null ? FriendRelationStatus.OUTPUT : FriendRelationStatus.FULL;
    }

    @Transactional
    public FriendRelationStatus removeFriendRequest(UUID targetId) {
        UUID currentUserId = getCurrentAccountId();
        FriendRequest outputRequest = friendRequestRepository.findByOwnerAccountIdAndTargetAccountId(currentUserId, targetId);
        friendRequestRepository.delete(outputRequest);
        FriendRequest incomingRequest = friendRequestRepository.findByOwnerAccountIdAndTargetAccountId(targetId, currentUserId);
        return incomingRequest == null ? FriendRelationStatus.NO_RELATION : FriendRelationStatus.INPUT;
    }

    @Transactional
    public FriendRelationStatus rejectFriendRequest(UUID targetId) {
        UUID currentUserId = getCurrentAccountId();
        FriendRequest inputRequest = friendRequestRepository.findByOwnerAccountIdAndTargetAccountId(targetId, currentUserId);
        friendRequestRepository.delete(inputRequest);
        return FriendRelationStatus.NO_RELATION;
    }

    @Transactional
    public List<UUID> getFriendIds(UUID accountId, int start, int end) {
        return friendRequestRepository.getFriendIds(accountId, start, end - start);
    }

    @Transactional
    public int countFriends(UUID accountId) {
        return friendRequestRepository.countFriends(accountId);
    }

    @Transactional
    public List<UUID> getInputFriends(UUID accountId, int start, int end) {
        return friendRequestRepository.getInputFriendsIds(accountId, start, end - start);
    }

    @Transactional
    public int countInputFriends(UUID accountId) {
        return friendRequestRepository.countInputRequest(accountId);
    }

    @Transactional
    public List<UUID> getOutputFriendIds(UUID accountId, int start, int end) {
        return friendRequestRepository.getOutputFriendIds(accountId, start, end - start);
    }

    @Transactional
    public int countOutputFriends(UUID accountId) {
        return friendRequestRepository.countOutputRequest(accountId);
    }

    public List<FriendRequest> getRequestsBetweenAccounts(UUID main, UUID target) {
        return friendRequestRepository.getRequestsBetweenAccount(main, target);
    }
}
