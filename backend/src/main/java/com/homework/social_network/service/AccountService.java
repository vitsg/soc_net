package com.homework.social_network.service;

import com.homework.social_network.dto.request.AccountRegistrationRequestDto;
import com.homework.social_network.dto.request.AccountUpdateRequestDto;
import com.homework.social_network.dto.request.PhotoRequestDto;
import com.homework.social_network.dto.response.AccountResponseDto;
import com.homework.social_network.dto.response.PageInfoAccessResponseDto;
import com.homework.social_network.dto.response.PageInfoResponseDto;
import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.Photo;
import com.homework.social_network.enums.UserRole;
import com.homework.social_network.repo.AccountRepository;
import com.homework.social_network.repo.PhotoRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.homework.social_network.utils.Utils.getDefaultImage;

@Service
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PhotoRepository photoRepository;

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
    public PageInfoResponseDto changePhoto(UUID id, PhotoRequestDto photoRequestDto) {
        MultipartFile photoFile = photoRequestDto.getPhoto();
        Account account = accountRepository.getById(id);
        if (photoFile == null || photoFile.getSize() == 0) {
            removePhoto(account);
        } else {
            addOrChangePhoto(photoFile, account);
        }
        Account changedAccount = accountRepository.save(account);
        return createPageInfo(changedAccount);
    }

    private void addOrChangePhoto(MultipartFile photoFile, Account account) {
        try {
            Photo forRemove = account.getPhoto();
            Optional.ofNullable(forRemove).ifPresent(p -> photoRepository.delete(p));
            Photo photo = new Photo();
            photo.setOwnerId(account.getId());
            photo.setMain(true);
            photo.setData(photoFile.getBytes());
            photo.setCreateDate(LocalDateTime.now());
            account.setPhoto(photo);
        } catch (Exception e) {
            log.error("Error by set photo", e);
            throw new RuntimeException("Error by set photo", e);
        }
    }

    private void removePhoto(Account account) {
        account.setPhoto(null);
        Photo forRemove = account.getPhoto();
        Optional.ofNullable(forRemove).ifPresent(p -> photoRepository.delete(p));
    }

    @Transactional
    public byte[] getAccountPhoto(UUID id) {
        Photo photo = photoRepository.getByOwnerIdAndMainIsTrue(id);
        if (photo == null) {
            return getDefaultImage();
        } else {
            return photo.getData();
        }
    }

    @Transactional
    public PageInfoResponseDto getPageInfo(UUID id) {
        Account account = accountRepository.getById(id);
        return createPageInfo(account);
    }

    private PageInfoResponseDto createPageInfo(Account account) {
        PageInfoResponseDto pageInfoResponseDto = new PageInfoResponseDto();
        pageInfoResponseDto.setAccount(modelMapper.map(account, AccountResponseDto.class));
        PageInfoAccessResponseDto pageInfoAccessResponseDto = new PageInfoAccessResponseDto();
        pageInfoAccessResponseDto.setCanChangePhoto(true);
        Optional.ofNullable(account.getPhoto()).ifPresent(p -> pageInfoAccessResponseDto.setCanRemovePhoto(true));
        pageInfoResponseDto.setAccesses(pageInfoAccessResponseDto);
        return pageInfoResponseDto;
    }
}
