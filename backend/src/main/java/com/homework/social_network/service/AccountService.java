package com.homework.social_network.service;

import com.homework.social_network.dto.request.AccountRegistrationRequestDto;
import com.homework.social_network.dto.request.AccountUpdateRequestDto;
import com.homework.social_network.dto.request.ChangeCredentialsRequestDto;
import com.homework.social_network.dto.request.PhotoRequestDto;
import com.homework.social_network.dto.response.account.access.AccountAccessResponseDto;
import com.homework.social_network.dto.response.account.info.AccountMetainfoResponseDto;
import com.homework.social_network.dto.response.account.list.AccountEntryResponseDto;
import com.homework.social_network.dto.response.account.list.AccountSimpleResponseDto;
import com.homework.social_network.dto.response.account.list.AccountsResponseDto;
import com.homework.social_network.dto.response.account.page.AccountPageInfoResponseDto;
import com.homework.social_network.dto.response.account.page.AccountPageResponseDto;
import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.BaseEntity;
import com.homework.social_network.entity.Photo;
import com.homework.social_network.enums.SexRelation;
import com.homework.social_network.enums.UserRole;
import com.homework.social_network.exception.CustomException;
import com.homework.social_network.repo.AccountRepository;
import com.homework.social_network.repo.PhotoRepository;
import com.homework.social_network.service.notification.NotificationService;
import com.homework.social_network.service.relation.AccountRelationService;
import com.homework.social_network.specification.account.AllAccountSpecificationHepler;
import com.homework.social_network.utils.CustomPageable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.homework.social_network.utils.Utils.getCurrentAccount;
import static com.homework.social_network.utils.Utils.getCurrentAccountId;
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
    private NotificationService notificationService;

    @Transactional
    public AccountPageResponseDto registration(AccountRegistrationRequestDto accountRegistrationRequestDto) {
        Account account = modelMapper.map(accountRegistrationRequestDto, Account.class);
        String registrationEmail = account.getEmail();
        if (accountRepository.findByEmail(registrationEmail) != null) {
            throw new CustomException("REGISTRATION_EMAIL_IS_EXISTS", "Email " + registrationEmail + " already exists");
        }
        account.setRole(UserRole.USER);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setCreateDate(LocalDateTime.now());
        Account savedAccount = accountRepository.save(account);
        notificationService.createNotificationAddPartner(savedAccount.getId(), null, savedAccount.getPartnerId());
        AccountPageResponseDto accountPageResponseDto = modelMapper.map(savedAccount, AccountPageResponseDto.class);
        accountPageResponseDto.setPartner(buildPartnerDto(savedAccount));
        return accountPageResponseDto;
    }

    @Transactional
    public AccountPageResponseDto findOne(UUID id) {
        Account account = accountRepository.getById(id);
        AccountPageResponseDto accountPageResponseDto = modelMapper.map(account, AccountPageResponseDto.class);
        accountPageResponseDto.setPartner(buildPartnerDto(account));
        return accountPageResponseDto;
    }

    @Transactional
    public Account getAccount(UUID id) {
        return accountRepository.getById(id);
    }

    @Transactional
    public AccountPageResponseDto update(AccountUpdateRequestDto accountUpdateRequestDto) {
        UUID currentAccountId = getCurrentAccountId();
        if (!currentAccountId.equals(accountUpdateRequestDto.getId())) {
            throw new CustomException("UPDATE_ACCOUNT_REJECT_ANOTHER_ACCOUNT", "Reject update another account");
        }
        Account account = accountRepository.getById(accountUpdateRequestDto.getId());
        UUID oldPartner = account.getPartnerId();
        modelMapper.map(accountUpdateRequestDto, account);
        Account savedAccount = accountRepository.save(account);
        UUID newPartner = savedAccount.getPartnerId();
        notificationService.createNotificationAddPartner(savedAccount.getId(), oldPartner, savedAccount.getPartnerId());
        AccountPageResponseDto accountPageResponseDto = modelMapper.map(savedAccount, AccountPageResponseDto.class);
        accountPageResponseDto.setPartner(buildPartnerDto(savedAccount));
        return accountPageResponseDto;
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
        Photo forRemove = account.getPhoto();
        Optional.ofNullable(forRemove).ifPresent(p -> photoRepository.delete(p));
        account.setPhoto(null);
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
        AccountPageResponseDto accountPageResponseDto = modelMapper.map(targetAccount, AccountPageResponseDto.class);
        accountPageResponseDto.setPartner(buildPartnerDto(targetAccount));
        accountPageInfoResponseDto.setAccount(accountPageResponseDto);

        AccountAccessResponseDto accountAccessResponseDto = new AccountAccessResponseDto();
        accountAccessResponseDto.setCanChangePhoto(true);
        Optional.ofNullable(targetAccount.getPhoto()).ifPresent(p -> accountAccessResponseDto.setCanRemovePhoto(true));
        accountAccessResponseDto.setCanModify(targetAccount.getId().equals(currentAccount.getId()));
        accountPageInfoResponseDto.setAccesses(accountAccessResponseDto);

        AccountMetainfoResponseDto metainfo = new AccountMetainfoResponseDto();
        metainfo.setFriendRelationStatus(accountRelationService.getRelationAccounts(currentAccount.getId(), targetAccount.getId()));
        accountPageInfoResponseDto.setInfo(metainfo);

        return accountPageInfoResponseDto;
    }

    private AccountSimpleResponseDto buildPartnerDto(Account account) {
        if (account.getFamilyStatus() == SexRelation.RELATION
                && Objects.nonNull(account.getPartnerId())
                && accountRepository.existsByIdAndPartnerId(account.getPartnerId(), account.getId())) {
            Account partner = accountRepository.findAllByIds(Collections.singletonList(account.getPartnerId()))
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Partner with id " + account.getPartnerId() + " not found"));
            AccountSimpleResponseDto simple = modelMapper.map(partner, AccountSimpleResponseDto.class);
            simple.setPhoto(getOrDefaultContentPhoto(account.getPhoto()));
            return simple;
        }
        return null;
    }

    @Transactional
    public AccountsResponseDto getAllAccount(int start, int end, String name, boolean withoutSelf) {
        Account currentAccount = getCurrentAccount();
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(Sort.Order.asc("lastName"));
        orders.add(Sort.Order.asc("firstName"));
        Sort sort = Sort.by(orders);
        List<UUID> excludedIds = new ArrayList<>();
        if (withoutSelf) {
            excludedIds.add(currentAccount.getId());
        }
        Pageable pageable = new CustomPageable(start, end - start, sort);
        List<AccountEntryResponseDto> accounts = accountRepository.findAll(AllAccountSpecificationHepler.allAccountSpecification(name, excludedIds), pageable)
                .stream()
                .map(account -> {
                    AccountSimpleResponseDto simple = modelMapper.map(account, AccountSimpleResponseDto.class);
                    simple.setPhoto(getOrDefaultContentPhoto(account.getPhoto()));
                    AccountMetainfoResponseDto info = AccountMetainfoResponseDto.builder()
                            .friendRelationStatus(accountRelationService.getRelationAccounts(currentAccount.getId(), account.getId()))
                            .build();
                    return AccountEntryResponseDto.builder()
                            .account(simple)
                            .info(info)
                            .build();
                })
                .collect(Collectors.toList());
        return AccountsResponseDto.builder()
                .accounts(accounts)
                .totalSize((int) accountRepository.count(AllAccountSpecificationHepler.countAllAccountSpecification(name, excludedIds)))
                .build();
    }

    @Transactional
    public Map<UUID, Account> getBatch(List<UUID> ids) {
        return accountRepository.findAllByIds(ids)
                .stream()
                .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));
    }

    @Transactional
    public AccountSimpleResponseDto getSimple(UUID id) {
        Account account = accountRepository.findAllByIds(Collections.singletonList(id)).get(0);
        AccountSimpleResponseDto simple = modelMapper.map(account, AccountSimpleResponseDto.class);
        simple.setPhoto(getOrDefaultContentPhoto(account.getPhoto()));
        return simple;
    }

    @Transactional
    public Account getByIdWithPhoto(UUID id) {
        return accountRepository.getByIdWithPhoto(id);
    }

    @Transactional
    public void changeCredentials(ChangeCredentialsRequestDto changeCredentialsRequestDto) {
        UUID currentAccountId = getCurrentAccountId();
        String oldPassword = changeCredentialsRequestDto.getOldPassword();
        String newPassword = changeCredentialsRequestDto.getNewPassword();
        if (!currentAccountId.equals(changeCredentialsRequestDto.getAccountId())) {
            throw new CustomException("CHANGE_CREDENTIALS_REJECT_ANOTHER_ACCOUNT", "Reject update another account");
        }
        Account account = accountRepository.getById(changeCredentialsRequestDto.getAccountId());
        String newEmail = Optional.ofNullable(changeCredentialsRequestDto.getEmail())
                .filter(StringUtils::isNotEmpty)
                .orElseThrow(() -> new CustomException("CHANGE_CREDENTIALS_EMPTY_PASSWORD_REJECT", "Email can not be empty"));
        if (!account.getEmail().equals(newEmail)) {
            if (accountRepository.findByEmail(newEmail) != null) {
                throw new CustomException("CHANGE_CREDENTIALS_EMAIL_IS_EXISTS", "Email already exists");
            }
            account.setEmail(newEmail);
        }

        if (StringUtils.isNotEmpty(oldPassword)) {
            if (passwordEncoder.matches(account.getPassword(), oldPassword)) {
                throw new CustomException("CHANGE_CREDENTIALS_INCORRECT_OLD_PASSWORD", "Incorrect old password");
            }
            if (StringUtils.isEmpty(newPassword)) {
                throw new CustomException("CHANGE_CREDENTIALS_EMPTY_NEW_PASSWORD", "New password can not be empty");
            }
            account.setPassword(passwordEncoder.encode(newPassword));
        }
        accountRepository.save(account);
    }

}
