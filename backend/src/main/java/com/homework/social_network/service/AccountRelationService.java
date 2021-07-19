package com.homework.social_network.service;

import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.FriendRequest;
import com.homework.social_network.enums.FriendRelationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountRelationService {

    @Autowired
    private FriendRequestService friendRequestService;

    public FriendRelationStatus getRelationAccounts(Account main, Account target) {
        if (main.getId().equals(target.getId())) {
            return FriendRelationStatus.SELF;
        }
        List<FriendRequest> friendRequests = friendRequestService.getRequestsBetweenAccounts(main.getId(), target.getId());
        if (friendRequests.size() == 2) {
            return FriendRelationStatus.FULL;
        } else if (friendRequests.size() == 0) {
            return FriendRelationStatus.NO_RELATION;
        } else {
            return getRelationStatusByRequest(main.getId(), target.getId(), friendRequests.get(0));
        }
    }

    private FriendRelationStatus getRelationStatusByRequest(UUID main, UUID target, FriendRequest request) {
        if (request.getOwnerAccount().getId().equals(main)) {
            return FriendRelationStatus.OUTPUT;
        } else {
            return FriendRelationStatus.INPUT;
        }
    }
}
