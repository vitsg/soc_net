package com.homework.social_network.repo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

public class CustomFriendRequestRepositoryImpl implements CustomFriendRequestRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UUID> getFriendIds(UUID ownerAccountId, int start, int limit) {
        return entityManager.createQuery("select f1.targetAccount.id from FriendRequest f1 where f1.ownerAccount.id = :ownerAccountId " +
                " and f1.targetAccount.id in (select f2.ownerAccount.id from FriendRequest f2 where f2.targetAccount.id = :ownerAccountId) " +
                "order by f1.createDate desc", UUID.class)
                .setFirstResult(start)
                .setMaxResults(limit)
                .setParameter("ownerAccountId", ownerAccountId)
                .getResultList();
    }

    @Override
    public List<UUID> getInputFriendsIds(UUID ownerAccountId, int start, int limit) {
        return entityManager.createQuery("select f1.ownerAccount.id from FriendRequest f1 where f1.targetAccount.id = :ownerAccountId " +
                " and f1.ownerAccount.id not in (select f2.targetAccount.id from FriendRequest f2 where f2.ownerAccount.id = :ownerAccountId) " +
                "order by f1.createDate desc", UUID.class)
                .setFirstResult(start)
                .setMaxResults(limit)
                .setParameter("ownerAccountId", ownerAccountId)
                .getResultList();
    }

    @Override
    public List<UUID> getOutputFriendIds(UUID ownerAccountId, int start, int limit) {
        return entityManager.createQuery("select f1.targetAccount.id from FriendRequest f1 where f1.ownerAccount.id = :ownerAccountId " +
                " and f1.targetAccount.id not in (select f2.ownerAccount.id from FriendRequest f2 where f2.targetAccount.id = :ownerAccountId) " +
                "order by f1.createDate desc", UUID.class)
                .setFirstResult(start)
                .setMaxResults(limit)
                .setParameter("ownerAccountId", ownerAccountId)
                .getResultList();
    }
}
