package com.homework.social_network.repo;

import com.homework.social_network.entity.Photo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

public class CustomRepositoryImpl implements CustomRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Photo> getByOwnerIdAndMainIsFalse(UUID ownerId, int start, int limit) {
        return entityManager.createQuery("select p from Photo p where p.ownerId = :ownerId and p.main = false order by p.createDate desc", Photo.class)
                .setFirstResult(start)
                .setMaxResults(limit)
                .setParameter("ownerId", ownerId)
                .getResultList();
    }
}
