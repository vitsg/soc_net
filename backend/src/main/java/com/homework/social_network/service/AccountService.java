package com.homework.social_network.service;

import com.homework.social_network.entity.Account;
import com.homework.social_network.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account registration(Account account) {
        return accountRepository.save(account);
    }

    public Account findOne(String id) {
        return accountRepository.getById(UUID.fromString(id));
    }
}
