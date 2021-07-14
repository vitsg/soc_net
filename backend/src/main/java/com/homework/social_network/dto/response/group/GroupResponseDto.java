package com.homework.social_network.dto.response.group;

import lombok.Data;

import java.util.UUID;

@Data
public class GroupResponseDto {

    private UUID id;
    private String name;
    private String theme;
    private String ownTheme;
    private String subTheme;
    private String description;
    private String photoName;
}
