package com.homework.social_network.repo;

import com.homework.social_network.entity.Group;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

public class CustomGroupRepositoryImpl implements CustomGroupRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Group> findAllByIds(List<UUID> ids) {
        return entityManager.createQuery("select g from Group g left join fetch g.photo where g.id in :ids", Group.class)
                .setParameter("ids", ids)
                .getResultList();
    }
}
