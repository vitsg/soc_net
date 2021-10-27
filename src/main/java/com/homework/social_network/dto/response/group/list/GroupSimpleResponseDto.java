package com.homework.social_network.dto.response.group.list;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupSimpleResponseDto {

    private UUID id;
    private String name;
    private byte[] photo;
}
