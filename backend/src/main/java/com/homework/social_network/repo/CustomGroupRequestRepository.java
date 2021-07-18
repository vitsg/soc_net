package com.homework.social_network.repo;

import com.homework.social_network.entity.GroupJoin;

import java.util.List;
import java.util.UUID;

public interface CustomGroupRequestRepository {

    List<GroupJoin> findByOwnerAccountId(UUID ownerAccountId, int start, int limit);
    List<GroupJoin> findByTargetGroupId(UUID targetGroupId, int start, int limit);
}
