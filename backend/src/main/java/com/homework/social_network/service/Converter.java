package com.homework.social_network.service;

import com.homework.social_network.dto.response.AccountResponseDto;
import com.homework.social_network.entity.Account;

public class Converter {

    public static AccountResponseDto convert(Account account) {
        AccountResponseDto accountResponseDto = new AccountResponseDto();
        accountResponseDto.setId(account.getId());
        accountResponseDto.setFirstName(account.getFirstName());
        accountResponseDto.setLastName(account.getLastName());
        accountResponseDto.setEmail(account.getEmail());
        accountResponseDto.setRole(account.getRole());
        accountResponseDto.setBirthDate(account.getBirthDate());
        return accountResponseDto;
    }
}
