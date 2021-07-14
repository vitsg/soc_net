package com.homework.social_network.service;

import com.homework.social_network.dto.request.AccountRegistrationRequestDto;
import com.homework.social_network.dto.request.AccountUpdateRequestDto;
import com.homework.social_network.dto.request.PhotoRequestDto;
import com.homework.social_network.dto.response.account.AccountPageInfoResponseDto;
import com.homework.social_network.dto.response.account.AccountPageMetainfoResponseDto;
import com.homework.social_network.dto.response.account.AccountResponseDto;
import com.homework.social_network.dto.response.account.AccountWithPhotoResponseDto;
import com.homework.social_network.dto.response.account.AccountsResponseDto;
import com.homework.social_network.dto.response.account.AccountPageInfoAccessResponseDto;
import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.BaseEntity;
import com.homework.social_network.entity.Photo;
import com.homework.social_network.enums.FriendRelationStatus;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.homework.social_network.utils.Utils.getCurrentAccount;
import static com.homework.social_network.utils.Utils.getOrDefaultContentPhoto;

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

    @Autowired
    private AccountRelationService accountRelationService;

    @Autowired
    private FriendRequestService friendRequestService;

    @Transactional
    public AccountResponseDto registration(AccountRegistrationRequestDto accountRegistrationRequestDto) {
        Account account = modelMapper.map(accountRegistrationRequestDto, Account.class);
        account.setRole(UserRole.USER);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setCreateDate(LocalDateTime.now());
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
    public AccountPageInfoResponseDto changePhoto(UUID id, PhotoRequestDto photoRequestDto) {
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
        return getOrDefaultContentPhoto(photo);
    }

    @Transactional
    public AccountPageInfoResponseDto getPageInfo(UUID id) {
        Account account = accountRepository.getById(id);
        return createPageInfo(account);
    }

    private AccountPageInfoResponseDto createPageInfo(Account targetAccount) {
        Account currentAccount = getCurrentAccount();
        AccountPageInfoResponseDto accountPageInfoResponseDto = new AccountPageInfoResponseDto();
        accountPageInfoResponseDto.setAccount(modelMapper.map(targetAccount, AccountResponseDto.class));

        AccountPageInfoAccessResponseDto accountPageInfoAccessResponseDto = new AccountPageInfoAccessResponseDto();
        accountPageInfoAccessResponseDto.setCanChangePhoto(true);
        Optional.ofNullable(targetAccount.getPhoto()).ifPresent(p -> accountPageInfoAccessResponseDto.setCanRemovePhoto(true));
        accountPageInfoAccessResponseDto.setCanModify(targetAccount.getId().equals(currentAccount.getId()));
        accountPageInfoResponseDto.setAccesses(accountPageInfoAccessResponseDto);

        AccountPageMetainfoResponseDto metainfo = new AccountPageMetainfoResponseDto();
        metainfo.setFriendRelationStatus(accountRelationService.getRelationAccounts(currentAccount, targetAccount));
        accountPageInfoResponseDto.setInfo(metainfo);

        return accountPageInfoResponseDto;
    }

    @Transactional
    public AccountsResponseDto getAllAccount(int start, int end) {
        List<AccountWithPhotoResponseDto> accounts = accountRepository.getAllAccounts(start, end - start)
                .stream()
                .map(account -> {
                    AccountWithPhotoResponseDto dto = modelMapper.map(account, AccountWithPhotoResponseDto.class);
                    dto.setPhoto(getOrDefaultContentPhoto(account.getPhoto()));
                    return dto;
                })
                .collect(Collectors.toList());
        AccountsResponseDto accountsResponseDto = new AccountsResponseDto();
        accountsResponseDto.setAccounts(accounts);
        accountsResponseDto.setTotalSize((int) accountRepository.count());
        return accountsResponseDto;
    }

    @Transactional
    public Map<UUID, AccountWithPhotoResponseDto> getBatch(List<UUID> ids) {
        return accountRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(BaseEntity::getId, g -> {
                    AccountWithPhotoResponseDto dto = modelMapper.map(g, AccountWithPhotoResponseDto.class);
                    dto.setPhoto(getOrDefaultContentPhoto(g.getPhoto()));
                    return dto;
                }));
    }

    @Transactional
    public FriendRelationStatus addFriend(UUID targetId) {
        return friendRequestService.addFriendRequest(targetId);
    }

    @Transactional
    public FriendRelationStatus removeFriend(UUID targetId) {
        return friendRequestService.removeFriendRequest(targetId);
    }

    @Transactional
    public AccountsResponseDto getFriends(UUID accountId, int start, int end) {
        List<UUID> accountIds = friendRequestService.getFriendIds(accountId, start, end);
        Map<UUID, AccountWithPhotoResponseDto> batch = getBatch(accountIds);
        List<AccountWithPhotoResponseDto> groups = accountIds.stream()
                .map(batch::get)
                .collect(Collectors.toList());
        AccountsResponseDto groupsResponseDto = new AccountsResponseDto();
        groupsResponseDto.setAccounts(groups);
        groupsResponseDto.setTotalSize(friendRequestService.countFriends(accountId));
        return groupsResponseDto;
    }

    @Transactional
    public AccountsResponseDto getInputFriends(UUID accountId, int start, int end) {
        List<UUID> groupIds = friendRequestService.getInputFriends(accountId, start, end);
        Map<UUID, AccountWithPhotoResponseDto> batch = getBatch(groupIds);
        List<AccountWithPhotoResponseDto> groups = groupIds.stream()
                .map(batch::get)
                .collect(Collectors.toList());
        AccountsResponseDto groupsResponseDto = new AccountsResponseDto();
        groupsResponseDto.setAccounts(groups);
        groupsResponseDto.setTotalSize(friendRequestService.countInputFriends(accountId));
        return groupsResponseDto;
    }

    @Transactional
    public AccountsResponseDto getOutputFriends(UUID accountId, int start, int end) {
        List<UUID> groupIds = friendRequestService.getOutputFriendIds(accountId, start, end);
        Map<UUID, AccountWithPhotoResponseDto> batch = getBatch(groupIds);
        List<AccountWithPhotoResponseDto> groups = groupIds.stream()
                .map(batch::get)
                .collect(Collectors.toList());
        AccountsResponseDto groupsResponseDto = new AccountsResponseDto();
        groupsResponseDto.setAccounts(groups);
        groupsResponseDto.setTotalSize(friendRequestService.countOutputFriends(accountId));
        return groupsResponseDto;
    }
}
