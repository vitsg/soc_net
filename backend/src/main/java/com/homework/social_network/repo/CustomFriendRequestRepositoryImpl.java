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
        return entityManager.createQuery("select f1.targetAccountId from FriendRequest f1 where f1.ownerAccountId = :ownerAccountId " +
                " and f1.targetAccountId in (select f2.ownerAccountId from FriendRequest f2 where f2.targetAccountId = :ownerAccountId) " +
                "order by f1.createDate desc", UUID.class)
                .setFirstResult(start)
                .setMaxResults(limit)
                .setParameter("ownerAccountId", ownerAccountId)
                .getResultList();
    }

    @Override
    public List<UUID> getInputFriendsIds(UUID ownerAccountId, int start, int limit) {
        return entityManager.createQuery("select f1.ownerAccountId from FriendRequest f1 where f1.targetAccountId = :ownerAccountId " +
                " and f1.ownerAccountId not in (select f2.targetAccountId from FriendRequest f2 where f2.ownerAccountId = :ownerAccountId) " +
                "order by f1.createDate desc", UUID.class)
                .setFirstResult(start)
                .setMaxResults(limit)
                .setParameter("ownerAccountId", ownerAccountId)
                .getResultList();
    }

    @Override
    public List<UUID> getOutputFriendIds(UUID ownerAccountId, int start, int limit) {
        return entityManager.createQuery("select f1.targetAccountId from FriendRequest f1 where f1.ownerAccountId = :ownerAccountId " +
                " and f1.targetAccountId not in (select f2.ownerAccountId from FriendRequest f2 where f2.targetAccountId = :ownerAccountId) " +
                "order by f1.createDate desc", UUID.class)
                .setFirstResult(start)
                .setMaxResults(limit)
                .setParameter("ownerAccountId", ownerAccountId)
                .getResultList();
    }
}
