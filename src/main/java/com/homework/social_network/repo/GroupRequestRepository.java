package com.homework.social_network.repo;

import com.homework.social_network.entity.GroupJoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GroupRequestRepository extends JpaRepository<GroupJoin, UUID> {

    GroupJoin findByOwnerAccountIdAndTargetGroupId(UUID ownerAccountId, UUID targetGroupId);

    List<GroupJoin> findByOwnerAccountIdAndTargetGroupIdIn(UUID ownerAccountId, List<UUID> targetGroupId);

    int countByOwnerAccountId(UUID ownerAccountId);

    int countByTargetGroupId(UUID targetGroupId);
}
