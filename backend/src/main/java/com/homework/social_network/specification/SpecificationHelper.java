package com.homework.social_network.specification;

import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.Group;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SpecificationHelper {

    public static Predicate getAccountNamePredicate(String name, CriteriaBuilder criteriaBuilder, Root<Account> root) {
        return criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + name + "%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + name + "%")
        );
    }

    public static Predicate getGroupNamePredicate(String name, CriteriaBuilder criteriaBuilder, Root<Group> root) {
        return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name + "%");

    }
}
