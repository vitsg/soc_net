package com.homework.social_network.service;

import com.homework.social_network.dto.request.AccountRegistrationRequestDto;
import com.homework.social_network.dto.request.AccountUpdateRequestDto;
import com.homework.social_network.dto.response.AccountResponseDto;
import com.homework.social_network.entity.Account;
import com.homework.social_network.enums.UserRole;
import com.homework.social_network.repo.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public AccountResponseDto registration(AccountRegistrationRequestDto accountRegistrationRequestDto) {
        Account account = modelMapper.map(accountRegistrationRequestDto, Account.class);
        account.setRole(UserRole.USER);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        Account savedAccount = accountRepository.save(account);
        return modelMapper.map(savedAccount, AccountResponseDto.class);
    }

    @Transactional
    public AccountResponseDto findOne(String id) {
        return modelMapper.map(accountRepository.getById(UUID.fromString(id)), AccountResponseDto.class);
    }

    @Transactional
    public AccountResponseDto update(AccountUpdateRequestDto accountUpdateRequestDto) {
        Account account = accountRepository.getById(accountUpdateRequestDto.getId());
        modelMapper.map(accountUpdateRequestDto, account);
        return modelMapper.map(accountRepository.save(account), AccountResponseDto.class);
    }
}
