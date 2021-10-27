package com.homework.social_network.specification.account;

import com.homework.social_network.entity.Account;
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

import static com.homework.social_network.specification.SpecificationHelper.getAccountNamePredicate;

public class GroupParticipantSpecification {

    public static Specification<Account> groupParticipantSpecification(UUID targetId, String name) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (criteriaQuery.getResultType() == Account.class) {
                root.fetch("photo", JoinType.LEFT);
            }
            return getGroupParticipantPredicate(criteriaBuilder, root, criteriaQuery, name, targetId);
        };
    }

    public static Specification<Account> countGroupParticipantSpecification(UUID targetId, String name) {
        return (root, criteriaQuery, criteriaBuilder) -> getGroupParticipantPredicate(criteriaBuilder, root, criteriaQuery, name, targetId);
    }

    private static Predicate getGroupParticipantPredicate(CriteriaBuilder criteriaBuilder, Root<Account> root, CriteriaQuery criteriaQuery, String name, UUID targetId) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.in(root.get("id")).value(getGroupParticipantSubquery(criteriaBuilder, criteriaQuery, targetId)));
        if (StringUtils.isNotEmpty(name)) {
            predicates.add(getAccountNamePredicate(name, criteriaBuilder, root));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private static Subquery<UUID> getGroupParticipantSubquery(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> rootQuery, UUID targetId) {
        Subquery<UUID> subqueryGroupParticipant = rootQuery.subquery(UUID.class);
        Root<GroupJoin> subqueryGroupParticipantRoot = subqueryGroupParticipant.from(GroupJoin.class);

        subqueryGroupParticipant.select(subqueryGroupParticipantRoot.get("ownerAccountId")).distinct(true);
        subqueryGroupParticipant.where(criteriaBuilder.equal(subqueryGroupParticipantRoot.get("targetGroupId"), targetId));
        return subqueryGroupParticipant;
    }
}
