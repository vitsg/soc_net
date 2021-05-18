package com.homework.social_network.service;

import com.homework.social_network.dto.AccountDto;
import com.homework.social_network.entity.Account;
import com.homework.social_network.enums.UserRole;
import com.homework.social_network.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.homework.social_network.service.Converter.convert;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public AccountDto registration(Account account) {
        account.setRole(UserRole.USER);
        return convert(accountRepository.save(account));
    }

    public Account findOne(String id) {
        return accountRepository.getById(UUID.fromString(id));
    }
}
