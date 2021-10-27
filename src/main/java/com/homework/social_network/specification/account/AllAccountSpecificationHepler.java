package com.homework.social_network.specification.account;

import com.homework.social_network.entity.Account;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.homework.social_network.specification.SpecificationHelper.getAccountNamePredicate;

public class AllAccountSpecificationHepler {

    public static Specification<Account> allAccountSpecification(String name, List<UUID> excludedIds) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (criteriaQuery.getResultType() == Account.class) {
                root.fetch("photo", JoinType.LEFT);
            }
            return getAllAccountPredicate(criteriaBuilder, root, name, excludedIds);
        };
    }

    public static Specification<Account> countAllAccountSpecification(String name, List<UUID> excludedIds) {
        return (root, criteriaQuery, criteriaBuilder) -> getAllAccountPredicate(criteriaBuilder, root, name, excludedIds);
    }

    private static Predicate getAllAccountPredicate(CriteriaBuilder criteriaBuilder, Root<Account> root, String name, List<UUID> excludedIds) {
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.isNotEmpty(name)) {
            predicates.add(getAccountNamePredicate(name, criteriaBuilder, root));
        }
        if (!CollectionUtils.isEmpty(excludedIds)) {
            predicates.add(criteriaBuilder.not(criteriaBuilder.in(root.get("id")).value(excludedIds)));
        }
        if (predicates.size() > 1) {
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        } else if (predicates.size() == 0) {
            return criteriaBuilder.conjunction();
        } else {
            return predicates.get(0);
        }
    }
}
