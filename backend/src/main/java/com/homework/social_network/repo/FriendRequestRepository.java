package com.homework.social_network.repo;

import com.homework.social_network.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, UUID>, CustomFriendRequestRepository {

    FriendRequest findByOwnerAccountIdAndTargetAccountId(UUID ownerAccountId, UUID targetAccountId);

    @Query("select count(f1) from FriendRequest f1 where f1.ownerAccountId = :ownerAccountId " +
            " and f1.targetAccountId in (select f2.ownerAccountId from FriendRequest f2 where f2.targetAccountId = :ownerAccountId)")
    int countFriends(UUID ownerAccountId);

    @Query("select count(f1) from FriendRequest f1 where f1.targetAccountId = :ownerAccountId " +
            " and f1.ownerAccountId not in (select f2.targetAccountId from FriendRequest f2 where f2.ownerAccountId = :ownerAccountId)")
    int countInputRequest(UUID ownerAccountId);

    @Query("select count(f1) from FriendRequest f1 where f1.ownerAccountId = :ownerAccountId " +
            " and f1.targetAccountId not in (select f2.ownerAccountId from FriendRequest f2 where f2.targetAccountId = :ownerAccountId)")
    int countOutputRequest(UUID ownerAccountId);
}
