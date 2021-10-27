package com.homework.social_network.dto.response.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountGroupOwnerResponseDto {

    private UUID id;
    private String firstName;
    private String lastName;
}
