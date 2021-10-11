package com.homework.social_network.service.request;

import com.homework.social_network.entity.FriendRequest;
import com.homework.social_network.enums.ErrorCode;
import com.homework.social_network.enums.FriendRelationStatus;
import com.homework.social_network.exception.CustomException;
import com.homework.social_network.repo.FriendRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.homework.social_network.utils.Utils.getCurrentAccountId;

@Service
public class FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Transactional
    public FriendRelationStatus addFriendRequest(UUID targetAccountId) {
        UUID currentAccountId = getCurrentAccountId();
        if (friendRequestRepository.findByOwnerAccountIdAndTargetAccountId(currentAccountId, targetAccountId) != null) {
            throw new CustomException(ErrorCode.FRIEND_REQUEST_ALREADY_EXISTS);
        }
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setOwnerAccountId(currentAccountId);
        friendRequest.setTargetAccountId(targetAccountId);
        friendRequest.setCreateDate(LocalDateTime.now());
        friendRequestRepository.save(friendRequest);
        FriendRequest inputRequest = friendRequestRepository.findByOwnerAccountIdAndTargetAccountId(targetAccountId, currentAccountId);
        return inputRequest == null ? FriendRelationStatus.OUTPUT : FriendRelationStatus.FULL;
    }

    @Transactional
    public FriendRelationStatus removeFriendRequest(UUID targetId) {
        UUID currentUserId = getCurrentAccountId();
        FriendRequest outputRequest = friendRequestRepository.findByOwnerAccountIdAndTargetAccountId(currentUserId, targetId);
        if (outputRequest == null) {
            throw new CustomException(ErrorCode.FRIEND_REQUEST_DOES_NOT_EXISTS);
        }
        friendRequestRepository.delete(outputRequest);
        FriendRequest incomingRequest = friendRequestRepository.findByOwnerAccountIdAndTargetAccountId(targetId, currentUserId);
        return incomingRequest == null ? FriendRelationStatus.NO_RELATION : FriendRelationStatus.INPUT;
    }

    @Transactional
    public FriendRelationStatus rejectFriendRequest(UUID targetId) {
        UUID currentUserId = getCurrentAccountId();
        FriendRequest outcomingRequest = friendRequestRepository.findByOwnerAccountIdAndTargetAccountId(currentUserId, targetId);
        if (outcomingRequest != null) {
            throw new CustomException(ErrorCode.FRIEND_REQUEST_ALREADY_ACCEPTED);
        }
        FriendRequest inputRequest = friendRequestRepository.findByOwnerAccountIdAndTargetAccountId(targetId, currentUserId);
        if (inputRequest == null) {
            throw new CustomException(ErrorCode.FRIEND_REQUEST_DOES_NOT_EXISTS);
        }
        friendRequestRepository.delete(inputRequest);
        return FriendRelationStatus.NO_RELATION;
    }


    public List<FriendRequest> getRequestsBetweenAccounts(UUID main, UUID target) {
        return friendRequestRepository.getRequestsBetweenAccount(main, target);
    }

}
