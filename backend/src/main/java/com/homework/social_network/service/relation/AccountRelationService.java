package com.homework.social_network.service.relation;

import com.homework.social_network.entity.FriendRequest;
import com.homework.social_network.enums.FriendRelationStatus;
import com.homework.social_network.service.request.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountRelationService {

    @Autowired
    private FriendRequestService friendRequestService;

    public FriendRelationStatus getRelationAccounts(UUID currentAccountId, UUID targetAccountId) {
        if (currentAccountId.equals(targetAccountId)) {
            return FriendRelationStatus.SELF;
        }
        List<FriendRequest> friendRequests = friendRequestService.getRequestsBetweenAccounts(currentAccountId, targetAccountId);
        if (friendRequests.size() == 2) {
            return FriendRelationStatus.FULL;
        } else if (friendRequests.size() == 0) {
            return FriendRelationStatus.NO_RELATION;
        } else {
            return getRelationStatusByRequest(currentAccountId, targetAccountId, friendRequests.get(0));
        }
    }

    private FriendRelationStatus getRelationStatusByRequest(UUID main, UUID target, FriendRequest request) {
        if (request.getOwnerAccountId().equals(main)) {
            return FriendRelationStatus.OUTPUT;
        } else {
            return FriendRelationStatus.INPUT;
        }
    }
}
