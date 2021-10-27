package com.homework.social_network.dto.response.group.list;

import com.homework.social_network.dto.response.group.info.GroupMetainfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupEntryResponseDto {

    private GroupSimpleResponseDto group;
    private GroupMetainfoResponseDto info;
}
