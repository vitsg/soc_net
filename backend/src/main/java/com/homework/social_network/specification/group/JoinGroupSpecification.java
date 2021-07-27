package com.homework.social_network.specification.group;

import com.homework.social_network.entity.Group;
import com.homework.social_network.entity.GroupJoin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.homework.social_network.specification.SpecificationHelper.getGroupNamePredicate;

public class JoinGroupSpecification {

    public static Specification<Group> joinGroupSpecification(UUID ownerAccountId, String name) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            root.fetch("photo", JoinType.LEFT);
            return getJoinGroupPredicate(criteriaBuilder, root, criteriaQuery, name, ownerAccountId);
        };
    }

    public static Specification<Group> countJoinGroupSpecification(UUID ownerAccountId, String name) {
        return (root, criteriaQuery, criteriaBuilder) -> getJoinGroupPredicate(criteriaBuilder, root, criteriaQuery, name, ownerAccountId);
    }

    private static Predicate getJoinGroupPredicate(CriteriaBuilder criteriaBuilder, Root<Group> root, CriteriaQuery criteriaQuery, String name, UUID ownerAccountId) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.in(root.get("id")).value(getJoinGroupSubquery(criteriaBuilder, criteriaQuery, ownerAccountId)));
        if (StringUtils.isNotEmpty(name)) {
            predicates.add(getGroupNamePredicate(name, criteriaBuilder, root));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private static Subquery<UUID> getJoinGroupSubquery(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> rootQuery, UUID ownerAccountId) {
        Subquery<UUID> subqueryJoinGroup = rootQuery.subquery(UUID.class);
        Root<GroupJoin> subqueryJoinGroupRoot = subqueryJoinGroup.from(GroupJoin.class);

        subqueryJoinGroup.select(subqueryJoinGroupRoot.get("targetGroupId")).distinct(true);
        subqueryJoinGroup.where(criteriaBuilder.equal(subqueryJoinGroupRoot.get("ownerAccountId"), ownerAccountId));
        return subqueryJoinGroup;
    }
}
