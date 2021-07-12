package com.homework.social_network.repo;

import com.homework.social_network.entity.Account;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

public class CustomAccountRepositoryImpl implements CustomAccountRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Account> getAllAccounts(int start, int limit) {
        return entityManager.createQuery("select a from Account a left join fetch a.photo order by a.createDate desc", Account.class)
                .setFirstResult(start)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Account> findAllByIds(List<UUID> ids) {
        return entityManager.createQuery("select a from Account a left join fetch a.photo where a.id in :ids", Account.class)
                .setParameter("ids", ids)
                .getResultList();
    }
}
