package com.homework.social_network.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class Photo extends BaseEntity {

    @Column(name = "onwer_id", nullable = false)
    private UUID ownerId;

    @Lob
    @Column(name = "data", nullable = false)
    private byte[] data;

    @Column(name = "main", nullable = false)
    private boolean main;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;
}
