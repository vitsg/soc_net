package com.homework.social_network.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
@Data
public class BaseEntity {

    @Id
    @GeneratedValue
    protected UUID id;
}
