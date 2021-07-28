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

public class InputFriendSpecification {

    public static Specification<Account> inputFriendSpecification(UUID ownerAccountId, String name) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (criteriaQuery.getResultType() == Account.class) {
                root.fetch("photo", JoinType.LEFT);
            }
            return getInputFriendPredicate(criteriaBuilder, root, criteriaQuery, name, ownerAccountId);
        };
    }

    public static Specification<Account> countInputFriendSpecification(UUID ownerAccountId, String name) {
        return (root, criteriaQuery, criteriaBuilder) -> getInputFriendPredicate(criteriaBuilder, root, criteriaQuery, name, ownerAccountId);
    }

    private static Predicate getInputFriendPredicate(CriteriaBuilder criteriaBuilder, Root<Account> root, CriteriaQuery criteriaQuery, String name, UUID ownerAccountId) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.in(root.get("id")).value(getInputFriendSubquery(criteriaBuilder, criteriaQuery, ownerAccountId)));
        if (StringUtils.isNotEmpty(name)) {
            predicates.add(getAccountNamePredicate(name, criteriaBuilder, root));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private static Subquery<UUID> getInputFriendSubquery(CriteriaBuilder criteriaBuilder, CriteriaQuery<?> rootQuery, UUID ownerAccountId) {
        Subquery<UUID> subqueryInputFriend = rootQuery.subquery(UUID.class);
        Root<FriendRequest> subqueryInputFriendRoot = subqueryInputFriend.from(FriendRequest.class);

        subqueryInputFriend.select(subqueryInputFriendRoot.get("ownerAccountId")).distinct(true);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(subqueryInputFriendRoot.get("targetAccountId"), ownerAccountId));
        predicates.add(criteriaBuilder.not(criteriaBuilder.in(subqueryInputFriendRoot.get("ownerAccountId"))
                .value(buildOutputFriendSubquery(criteriaBuilder, subqueryInputFriend, ownerAccountId))));
        subqueryInputFriend.where(predicates.toArray(new Predicate[0]));
        return subqueryInputFriend;
    }

    private static Subquery<UUID> buildOutputFriendSubquery(CriteriaBuilder criteriaBuilder, Subquery<UUID> subqueryInputFriend, UUID ownerAccountId) {
        Subquery<UUID> subqueryOutputFriend = subqueryInputFriend.subquery(UUID.class);
        Root<FriendRequest> subquerOutputFriendRoot = subqueryOutputFriend.from(FriendRequest.class);
        subqueryOutputFriend.select(subquerOutputFriendRoot.get("targetAccountId")).distinct(true);
        subqueryOutputFriend.where(criteriaBuilder.equal(subquerOutputFriendRoot.get("ownerAccountId"), ownerAccountId));
        return subqueryOutputFriend;
    }
}
