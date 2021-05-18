package com.homework.social_network.service;

import com.homework.social_network.dto.AccountDto;
import com.homework.social_network.dto.CurrentStatusDto;
import com.homework.social_network.entity.Account;
import com.homework.social_network.enums.Access;
import com.homework.social_network.enums.UserRole;
import com.homework.social_network.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.homework.social_network.service.Converter.convert;

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

    public CurrentStatusDto getCurrentStatusDto() {
        Account principal = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Access> accesses = new ArrayList<>();
        switch (principal.getRole()) {
            case ADMIN:
            case USER:
                accesses.add(Access.ALL);
                break;
            default:
                break;
        }
        return createCurrentStatusDto(principal, accesses);
    }

    private CurrentStatusDto createCurrentStatusDto(Account account, List<Access> accesses) {
        CurrentStatusDto currentStatusDto = new CurrentStatusDto();
        currentStatusDto.setCurrentAccount(convert(account));
        currentStatusDto.setAccesses(accesses);
        return currentStatusDto;
    }
}
