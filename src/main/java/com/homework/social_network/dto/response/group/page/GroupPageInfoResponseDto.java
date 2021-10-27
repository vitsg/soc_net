package com.homework.social_network.dto.response.group.page;

import com.homework.social_network.dto.response.group.info.GroupMetainfoResponseDto;
import com.homework.social_network.dto.response.group.GroupWithPhotoPageResponseDto;
import com.homework.social_network.dto.response.group.access.GroupAccessResponseDto;
import lombok.Data;

@Data
public class GroupPageInfoResponseDto {
    private GroupWithPhotoPageResponseDto group;
    private GroupAccessResponseDto accesses;
    private GroupMetainfoResponseDto info;
}
