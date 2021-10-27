package com.homework.social_network.service;

import com.homework.social_network.dto.response.account.page.AccountPageResponseDto;
import com.homework.social_network.entity.Account;

public class Converter {

    public static AccountPageResponseDto convert(Account account) {
        AccountPageResponseDto accountPageResponseDto = new AccountPageResponseDto();
        accountPageResponseDto.setId(account.getId());
        accountPageResponseDto.setFirstName(account.getFirstName());
        accountPageResponseDto.setLastName(account.getLastName());
        accountPageResponseDto.setEmail(account.getEmail());
        accountPageResponseDto.setRole(account.getRole());
        accountPageResponseDto.setBirthDate(account.getBirthDate());
        return accountPageResponseDto;
    }
}
