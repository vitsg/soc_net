package com.homework.social_network.service;

import com.homework.social_network.dto.response.AccountResponseDto;
import com.homework.social_network.dto.response.CurrentStatusDto;
import com.homework.social_network.entity.Account;
import com.homework.social_network.enums.Access;
import com.homework.social_network.repo.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.homework.social_network.service.Converter.convert;

@Service
public class AuthService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public Account findByEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new RuntimeException("Account with email " + email + " not found");
        }
        return account;
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
        currentStatusDto.setCurrentAccount(modelMapper.map(account, AccountResponseDto.class));
        currentStatusDto.setAccesses(accesses);
        return currentStatusDto;
    }
}
