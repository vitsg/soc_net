package com.homework.social_network.entity;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "sn_group")
public class Group extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "theme")
    private String theme;

    @Column(name = "own_theme")
    private String ownTheme;

    @Column(name = "sub_theme")
    private String subTheme;

    @Column(name = "description")
    private String description;

    @Column(name = "photo_name")
    private String photoName;

    @JoinColumn(name = "photo_id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Photo photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Account owner;

}
