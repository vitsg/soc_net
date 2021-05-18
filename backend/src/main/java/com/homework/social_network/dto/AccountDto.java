package com.homework.social_network.dto;

import com.homework.social_network.enums.UserRole;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AccountDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private UserRole role;
}
