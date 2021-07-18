package com.homework.social_network.repo;

import com.homework.social_network.entity.GroupJoin;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

public class CustomGroupRequestRepositoryImpl implements CustomGroupRequestRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<GroupJoin> findByOwnerAccountId(UUID ownerAccountId, int start, int limit) {
        return entityManager.createQuery("select g from GroupJoin g where g.ownerAccount.id = :ownerAccountId order by g.createDate desc", GroupJoin.class)
                .setFirstResult(start)
                .setMaxResults(limit)
                .setParameter("ownerAccountId", ownerAccountId)
                .getResultList();
    }

    @Override
    public List<GroupJoin> findByTargetGroupId(UUID targetGroupId, int start, int limit) {
        return entityManager.createQuery("select g from GroupJoin g where g.targetGroup.id = :targetGroupId order by g.createDate desc", GroupJoin.class)
                .setFirstResult(start)
                .setMaxResults(limit)
                .setParameter("targetGroupId", targetGroupId)
                .getResultList();
    }
}
