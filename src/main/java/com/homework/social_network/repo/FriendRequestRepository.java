package com.homework.social_network.repo;

import com.homework.social_network.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, UUID> {

    FriendRequest findByOwnerAccountIdAndTargetAccountId(UUID ownerAccountId, UUID targetAccountId);

    @Query("select f from FriendRequest f where (f.ownerAccountId = :main and f.targetAccountId = :target)" +
            "or (f.targetAccountId = :main and f.ownerAccountId = :target)")
    List<FriendRequest> getRequestsBetweenAccount(UUID main, UUID target);
}
