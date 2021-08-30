package com.homework.social_network.dto.response.account.page;

import com.homework.social_network.dto.response.account.list.AccountSimpleResponseDto;
import com.homework.social_network.enums.Sex;
import com.homework.social_network.enums.SexRelation;
import com.homework.social_network.enums.UserRole;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AccountPageResponseDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private UserRole role;
    private Sex sex;
    private String city;
    private SexRelation familyStatus;
    private AccountSimpleResponseDto partner;
    private String phone;
    private String employment;
    private String description;
}
