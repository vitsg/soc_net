package com.homework.social_network.service;

import com.homework.social_network.dto.AccountDto;
import com.homework.social_network.entity.Account;

public class Converter {

    public static AccountDto convert(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setFirstName(account.getFirstName());
        accountDto.setLastName(account.getLastName());
        accountDto.setEmail(account.getEmail());
        accountDto.setRole(account.getRole());
        accountDto.setBirthDate(account.getBirthDate());
        return accountDto;
    }
}
