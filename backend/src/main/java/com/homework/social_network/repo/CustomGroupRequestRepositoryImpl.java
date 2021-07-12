package com.homework.social_network.repo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

public class CustomGroupRequestRepositoryImpl implements CustomGroupRequestRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UUID> findByOwnerAccountId(UUID ownerAccountId, int start, int limit) {
        return entityManager.createQuery("select g.targetGroupId from GroupRequest g where g.ownerAccountId = :ownerAccountId order by g.createDate desc", UUID.class)
                .setFirstResult(start)
                .setMaxResults(limit)
                .setParameter("ownerAccountId", ownerAccountId)
                .getResultList();
    }
}
