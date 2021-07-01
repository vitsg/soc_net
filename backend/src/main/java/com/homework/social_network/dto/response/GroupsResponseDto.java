package com.homework.social_network.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class GroupsResponseDto {

    private List<GroupWithPhotoResponseDto> groups;
    private int totalSize;
}
