package com.homework.social_network.repo;

import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.Group;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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
}
