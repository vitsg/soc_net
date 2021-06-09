package com.homework.social_network.service;

import com.homework.social_network.dto.request.AccountRegistrationRequestDto;
import com.homework.social_network.dto.request.AccountUpdateRequestDto;
import com.homework.social_network.dto.request.PhotoRequestDto;
import com.homework.social_network.dto.response.AccountResponseDto;
import com.homework.social_network.entity.Account;
import com.homework.social_network.enums.UserRole;
import com.homework.social_network.repo.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.homework.social_network.utils.Utils.getDefaultImage;

@Service
@Slf4j
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
    public AccountResponseDto findOne(String id, boolean withPhoto) {
        Account account = accountRepository.getById(UUID.fromString(id));
        return modelMapper.map(account, AccountResponseDto.class);
    }

    @Transactional
    public AccountResponseDto update(AccountUpdateRequestDto accountUpdateRequestDto) {
        Account account = accountRepository.getById(accountUpdateRequestDto.getId());
        modelMapper.map(accountUpdateRequestDto, account);
        return modelMapper.map(accountRepository.save(account), AccountResponseDto.class);
    }

    @Transactional
    public void changePhoto(UUID id, PhotoRequestDto photoRequestDto) {
        Account account = accountRepository.getById(id);
        try {
            account.setPhoto(photoRequestDto.getPhoto() == null ? null : photoRequestDto.getPhoto().getBytes());
        } catch (Exception e) {
            log.error("Error by set photo", e);
            throw new RuntimeException("Error by set photo", e);
        }
    }

    @Transactional
    public byte[] getAccountPhoto(UUID id) {
        Account account = accountRepository.getById(id);
        byte[] photo = account.getPhoto();
        if (photo == null || photo.length == 0) {
            return getDefaultImage();
        } else {
            return photo;
        }
    }
}
