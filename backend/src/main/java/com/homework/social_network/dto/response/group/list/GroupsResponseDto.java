package com.homework.social_network.dto.response.group.list;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupsResponseDto {

    private List<GroupEntryResponseDto> groups;
    private int totalSize;
}
