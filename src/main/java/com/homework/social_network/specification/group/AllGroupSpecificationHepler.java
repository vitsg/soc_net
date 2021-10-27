package com.homework.social_network.specification.group;

import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.Group;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static com.homework.social_network.specification.SpecificationHelper.getGroupNamePredicate;

public class AllGroupSpecificationHepler {

    public static Specification<Group> allGroupSpecification(String name) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (criteriaQuery.getResultType() == Account.class) {
                root.fetch("photo", JoinType.LEFT);
            }
            return getAllGroupPredicate(criteriaBuilder, root, name);
        };
    }

    public static Specification<Group> countAllGroupSpecification(String name) {
        return (root, criteriaQuery, criteriaBuilder) -> getAllGroupPredicate(criteriaBuilder, root, name);
    }

    private static Predicate getAllGroupPredicate(CriteriaBuilder criteriaBuilder, Root<Group> root, String name) {
        if (StringUtils.isNotEmpty(name)) {
            return getGroupNamePredicate(name, criteriaBuilder, root);
        }
        return criteriaBuilder.conjunction();
    }
}
