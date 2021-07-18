package com.homework.social_network.dto.response.account.list;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountSimpleResponseDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private byte[] photo;
}
