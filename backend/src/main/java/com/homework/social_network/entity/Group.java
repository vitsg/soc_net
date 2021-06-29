package com.homework.social_network.entity;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
    @OneToOne(cascade = CascadeType.ALL)
    private Photo photo;
}
