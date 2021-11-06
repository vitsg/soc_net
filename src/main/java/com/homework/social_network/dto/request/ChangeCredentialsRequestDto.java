package com.homework.social_network.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeCredentialsRequestDto {

    private UUID accountId;
    private String email;
    private String passwordForEmailChange;
    private String oldPassword;
    private String newPassword;
}
