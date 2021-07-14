package com.homework.social_network.dto.response.account;

import com.homework.social_network.enums.Sex;
import com.homework.social_network.enums.UserRole;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AccountResponseDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private UserRole role;
    private Sex sex;
    private String city;
    private String familyStatus;
    private String phone;
    private String employment;
    private String description;
}
