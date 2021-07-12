package com.homework.social_network.repo;

import com.homework.social_network.entity.GroupRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupRequestRepository extends JpaRepository<GroupRequest, UUID>, CustomGroupRequestRepository {

    GroupRequest findByOwnerAccountIdAndTargetGroupId(UUID ownerAccountId, UUID targetGroupId);

    int countByOwnerAccountId(UUID ownerAccountId);
}
