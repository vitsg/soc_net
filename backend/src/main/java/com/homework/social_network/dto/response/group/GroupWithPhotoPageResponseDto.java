package com.homework.social_network.dto.response.group;

import com.homework.social_network.dto.response.group.page.GroupPageResponseDto;
import lombok.Data;

@Data
public class GroupWithPhotoPageResponseDto extends GroupPageResponseDto {
    private byte[] photo;
}
