package com.homework.social_network.dto.response;

import lombok.Data;

@Data
public class GroupPageInfoResponseDto {
    private GroupWithPhotoResponseDto group;
    private PageInfoAccessResponseDto accesses;
}
