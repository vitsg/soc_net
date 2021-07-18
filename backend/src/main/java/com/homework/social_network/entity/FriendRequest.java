package com.homework.social_network.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.UUID;

@Entity
@Data
@Table(name = "friend_request", uniqueConstraints = @UniqueConstraint(columnNames={"owner_account_id", "target_account_id"}))
public class FriendRequest extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "owner_account_id")
    private Account ownerAccount;

    @ManyToOne
    @JoinColumn(name = "target_account_id")
    private Account targetAccount;

}
