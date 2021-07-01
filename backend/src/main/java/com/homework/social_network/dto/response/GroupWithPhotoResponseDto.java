package com.homework.social_network.dto.response;

import lombok.Data;

@Data
public class GroupWithPhotoResponseDto extends GroupResponseDto {
    private byte[] photo;
}
