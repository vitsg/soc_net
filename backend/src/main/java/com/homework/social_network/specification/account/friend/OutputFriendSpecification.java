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

public class OutputFriendSpecification {

    public static Specification<Account> outputFriendSpecification(UUID ownerAccountId, String name) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (criteriaQuery.getResultType() == Account.class) {
                root.fetch("photo", JoinType.LEFT);
            }
            return getOutputFriendPredicate(criteriaBuilder, root, criteriaQuery, name, ownerAccountId);
        };
    }

    public static Specification<Account> countOutputFriendSpecification(UUID ownerAccountId, String name) {
        return (root, criteriaQuery, criteriaBuilder) -> getOutputFriendPredicate(criteriaBuilder, root, criteriaQuery, name, ownerAccountId);
    }

    private static Predicate getOutputFriendPredicate(CriteriaBuilder criteriaBuilder, Root<Account> root, CriteriaQuery criteriaQuery, String name, UUID ownerAccountId) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.in(root.get("id")).value(getOutputFriendSubquery(criteriaBuilder, criteriaQuery, ownerAccountId)));
        if (StringUtils.isNotEmpty(name)) {
            predicates.add(getAccountNamePredicate(name, criteriaBuilder, root));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private static Subquery<UUID> getOutputFriendSubquery(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> rootQuery, UUID ownerAccountId) {
        Subquery<UUID> subqueryInputFriend = rootQuery.subquery(UUID.class);
        Root<FriendRequest> subqueryInputFriendRoot = subqueryInputFriend.from(FriendRequest.class);

        subqueryInputFriend.select(subqueryInputFriendRoot.get("targetAccountId")).distinct(true);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(subqueryInputFriendRoot.get("ownerAccountId"), ownerAccountId));
        predicates.add(criteriaBuilder.not(criteriaBuilder.in(subqueryInputFriendRoot.get("targetAccountId"))
                .value(buildInputFriendSubquery(criteriaBuilder, subqueryInputFriend, ownerAccountId))));
        subqueryInputFriend.where(predicates.toArray(new Predicate[0]));
        return subqueryInputFriend;
    }

    private static Subquery<UUID> buildInputFriendSubquery(CriteriaBuilder criteriaBuilder, Subquery<UUID> subqueryInputFriend, UUID ownerAccountId) {
        Subquery<UUID> subqueryOutputFriend = subqueryInputFriend.subquery(UUID.class);
        Root<FriendRequest> subquerOutputFriendRoot = subqueryOutputFriend.from(FriendRequest.class);
        subqueryOutputFriend.select(subquerOutputFriendRoot.get("ownerAccountId")).distinct(true);
        subqueryOutputFriend.where(criteriaBuilder.equal(subquerOutputFriendRoot.get("targetAccountId"), ownerAccountId));
        return subqueryOutputFriend;
    }
}
