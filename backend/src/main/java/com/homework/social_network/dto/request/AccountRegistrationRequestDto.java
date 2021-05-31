package com.homework.social_network.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AccountRegistrationRequestDto {

    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String password;
}
