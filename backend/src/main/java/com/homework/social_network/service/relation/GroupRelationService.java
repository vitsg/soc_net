package com.homework.social_network.service.relation;

import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.Group;
import com.homework.social_network.enums.GroupRelationStatus;
import com.homework.social_network.service.request.GroupRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupRelationService {

    @Autowired
    private GroupRequestService groupRequestService;

    public GroupRelationStatus getRelationGroup(Account account, Group group) {
        if (account.getId().equals(group.getOwner().getId())) {
            return GroupRelationStatus.OWNER;
        }
        return groupRequestService.getRequestGroup(account.getId(), group.getId()) == null ? GroupRelationStatus.NONE : GroupRelationStatus.PARTICIPANT;
    }
}
