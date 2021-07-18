package com.homework.social_network.dto.response.group.info;

import com.homework.social_network.enums.GroupRelationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMetainfoResponseDto {

    private GroupRelationStatus groupRelationStatus;
}
