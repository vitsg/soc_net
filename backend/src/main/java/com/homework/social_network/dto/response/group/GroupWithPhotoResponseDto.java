package com.homework.social_network.dto.response.group;

import com.homework.social_network.dto.response.group.GroupResponseDto;
import lombok.Data;

@Data
public class GroupWithPhotoResponseDto extends GroupResponseDto {
    private byte[] photo;
}
