package com.homework.social_network.entity;

import com.homework.social_network.enums.GroupRelationStatus;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.UUID;

@Entity
@Data
@Table(name = "group_request", uniqueConstraints = @UniqueConstraint(columnNames={"owner_account_id", "target_group_id"}))
public class GroupJoin extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "owner_account_id")
    private Account ownerAccount;

    @ManyToOne
    @JoinColumn(name = "target_group_id")
    private Group targetGroup;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private GroupRelationStatus status;
}
