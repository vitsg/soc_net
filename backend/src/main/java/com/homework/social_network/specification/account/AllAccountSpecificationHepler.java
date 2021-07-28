package com.homework.social_network.specification.account;

import com.homework.social_network.entity.Account;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.UUID;

import static com.homework.social_network.specification.SpecificationHelper.getAccountNamePredicate;

public class AllAccountSpecificationHepler {

    public static Specification<Account> allAccountSpecification(String name) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (criteriaQuery.getResultType() == Account.class) {
                root.fetch("photo", JoinType.LEFT);
            }
            return getAllAccountPredicate(criteriaBuilder, root, name);
        };
    }

    public static Specification<Account> countAllAccountSpecification(String name) {
        return (root, criteriaQuery, criteriaBuilder) -> getAllAccountPredicate(criteriaBuilder, root, name);
    }

    private static Predicate getAllAccountPredicate(CriteriaBuilder criteriaBuilder, Root<Account> root, String name) {
        if (StringUtils.isNotEmpty(name)) {
            return getAccountNamePredicate(name, criteriaBuilder, root);
        }
        return criteriaBuilder.conjunction();
    }
}
