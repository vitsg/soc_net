package com.homework.social_network.dto.request;

import com.homework.social_network.enums.Sex;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class AccountUpdateRequestDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Sex sex;
    private String city;
    private String familyStatus;
    private String phone;
    private String employment;
    private String description;
}
