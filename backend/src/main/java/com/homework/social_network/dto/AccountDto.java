package com.homework.social_network.dto;

import com.homework.social_network.enums.UserRole;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AccountDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime birthDate;
    private UserRole role;
}
