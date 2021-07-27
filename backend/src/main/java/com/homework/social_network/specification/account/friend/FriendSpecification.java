package com.homework.social_network.specification.account.friend;

import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.FriendRequest;
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

public class FriendSpecification {

    public static Specification<Account> friendSpecification(UUID ownerAccountId, String name) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            root.fetch("photo", JoinType.LEFT);
            return getFriendPredicate(criteriaBuilder, root, criteriaQuery, name, ownerAccountId);
        };
    }

    public static Specification<Account> countFriendSpecification(UUID ownerAccountId, String name) {
        return (root, criteriaQuery, criteriaBuilder) -> getFriendPredicate(criteriaBuilder, root, criteriaQuery, name, ownerAccountId);
    }

    private static Predicate getFriendPredicate(CriteriaBuilder criteriaBuilder, Root<Account> root, CriteriaQuery criteriaQuery, String name, UUID ownerAccountId) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.in(root.get("id")).value(getFriendSubquery(criteriaBuilder, criteriaQuery, ownerAccountId)));
        if (StringUtils.isNotEmpty(name)) {
            predicates.add(getAccountNamePredicate(name, criteriaBuilder, root));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private static Subquery<UUID> getFriendSubquery(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> rootQuery, UUID ownerAccountId) {
        Subquery<UUID> subqueryOutputFriend = rootQuery.subquery(UUID.class);
        Root<FriendRequest> subqueryOutputFriendRoot = subqueryOutputFriend.from(FriendRequest.class);

        subqueryOutputFriend.select(subqueryOutputFriendRoot.get("targetAccountId")).distinct(true);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(subqueryOutputFriendRoot.get("ownerAccountId"), ownerAccountId));
        predicates.add(criteriaBuilder.in(subqueryOutputFriendRoot.get("targetAccountId")).value(buildInputFriendSubquery(criteriaBuilder, subqueryOutputFriend, ownerAccountId)));
        subqueryOutputFriend.where(predicates.toArray(new Predicate[0]));
        return subqueryOutputFriend;
    }

    private static Subquery<UUID> buildInputFriendSubquery(CriteriaBuilder criteriaBuilder, Subquery<UUID> subqueryFriend, UUID ownerAccountId) {
        Subquery<UUID> subqueryInputFriend = subqueryFriend.subquery(UUID.class);
        Root<FriendRequest> subqueryInputFriendRoot = subqueryInputFriend.from(FriendRequest.class);
        subqueryInputFriend.select(subqueryInputFriendRoot.get("ownerAccountId")).distinct(true);
        subqueryInputFriend.where(criteriaBuilder.equal(subqueryInputFriendRoot.get("targetAccountId"), ownerAccountId));
        return subqueryInputFriend;
    }
}
