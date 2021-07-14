package com.homework.social_network.dto.response.group;

import lombok.Data;

@Data
public class GroupPageInfoResponseDto {
    private GroupWithPhotoResponseDto group;
    private GroupPageInfoAccessResponseDto accesses;
    private GroupPageMetainfoResponseDto info;
}
