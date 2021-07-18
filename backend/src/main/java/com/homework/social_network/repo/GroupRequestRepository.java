package com.homework.social_network.repo;

import com.homework.social_network.entity.GroupJoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupRequestRepository extends JpaRepository<GroupJoin, UUID>, CustomGroupRequestRepository {

    GroupJoin findByOwnerAccountIdAndTargetGroupId(UUID ownerAccountId, UUID targetGroupId);

    int countByOwnerAccountId(UUID ownerAccountId);

    int countByTargetGroupId(UUID targetGroupId);
}
