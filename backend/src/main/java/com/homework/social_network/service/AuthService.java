package com.homework.social_network.service;

import com.homework.social_network.entity.Account;
import com.homework.social_network.enums.Access;
import com.homework.social_network.enums.UserRole;
import com.homework.social_network.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private AccountRepository accountRepository;

    public Account findByEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new RuntimeException("Account with email " + email + " not found");
        }
        return accountRepository.findByEmail(email);
    }

    public List<Access> getAccessForCurrentUser() {
        Account principal = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        switch (principal.getRole()) {
            case ADMIN:
            case USER:
                return Collections.singletonList(Access.ALL);
            default:
                return Collections.emptyList();
        }
    }
}
