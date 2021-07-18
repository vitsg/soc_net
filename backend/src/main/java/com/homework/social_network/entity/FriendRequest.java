package com.homework.social_network.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.UUID;

@Entity
@Data
@Table(name = "friend_request", indexes = @Index(name = "unique_friend_request", columnList = "owner_account_id, target_account_id", unique = true))
public class FriendRequest extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "owner_account_id")
    private Account ownerAccount;

    @ManyToOne
    @JoinColumn(name = "target_account_id")
    private Account targetAccount;

}
