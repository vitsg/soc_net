package com.homework.social_network.service;

import com.homework.social_network.dto.AccountDto;
import com.homework.social_network.entity.Account;
import com.homework.social_network.enums.UserRole;
import com.homework.social_network.repo.AccountRepository;
import com.homework.social_network.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.homework.social_network.service.Converter.convert;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AccountDto registration(Account account) {
        account.setRole(UserRole.USER);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        Account savedAccount = accountRepository.save(account);
        return convert(savedAccount);
    }

    public Account findOne(String id) {
        return accountRepository.getById(UUID.fromString(id));
    }
}
