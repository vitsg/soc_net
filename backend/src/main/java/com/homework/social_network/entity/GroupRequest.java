package com.homework.social_network.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.UUID;

@Entity
@Data
@Table(name = "group_request", uniqueConstraints = @UniqueConstraint(columnNames={"owner_account_id", "target_group_id"}))
public class GroupRequest extends BaseEntity {

    @Column(name = "owner_account_id")
    private UUID ownerAccountId;

    @Column(name = "target_group_id")
    private UUID targetGroupId;
}
