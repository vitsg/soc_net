package com.homework.social_network.repo;

import com.homework.social_network.entity.Group;
import com.homework.social_network.entity.Photo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CustomGroupRepositoryImpl implements CustomGroupRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Group> getAllGroups(int start, int limit) {
        return entityManager.createQuery("select g from Group g order by g.createDate desc", Group.class)
                .setFirstResult(start)
                .setMaxResults(limit)
                .getResultList();
    }
}
