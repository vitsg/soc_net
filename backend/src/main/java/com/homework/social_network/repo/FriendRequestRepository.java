package com.homework.social_network.repo;

import com.homework.social_network.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, UUID>, CustomFriendRequestRepository {

    FriendRequest findByOwnerAccountIdAndTargetAccountId(UUID ownerAccountId, UUID targetAccountId);

    @Query("select count(f1) from FriendRequest f1 where f1.ownerAccount.id = :ownerAccountId " +
            " and f1.targetAccount.id in (select f2.ownerAccount.id from FriendRequest f2 where f2.targetAccount.id = :ownerAccountId)")
    int countFriends(UUID ownerAccountId);

    @Query("select count(f1) from FriendRequest f1 where f1.targetAccount.id = :ownerAccountId " +
            " and f1.ownerAccount.id not in (select f2.targetAccount.id from FriendRequest f2 where f2.ownerAccount.id = :ownerAccountId)")
    int countInputRequest(UUID ownerAccountId);

    @Query("select count(f1) from FriendRequest f1 where f1.ownerAccount.id = :ownerAccountId " +
            " and f1.targetAccount.id not in (select f2.ownerAccount.id from FriendRequest f2 where f2.targetAccount.id = :ownerAccountId)")
    int countOutputRequest(UUID ownerAccountId);

    @Query("select f from FriendRequest f where (f.ownerAccount.id = :main and f.targetAccount.id = :target)" +
            "or (f.targetAccount.id = :main and f.ownerAccount.id = :target)")
    List<FriendRequest> getRequestsBetweenAccount(UUID main, UUID target);
}
