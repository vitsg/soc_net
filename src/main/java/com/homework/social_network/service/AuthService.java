package com.homework.social_network.service;

import com.homework.social_network.dto.response.account.page.AccountPageResponseDto;
import com.homework.social_network.dto.response.CurrentStatusDto;
import com.homework.social_network.entity.Account;
import com.homework.social_network.enums.Access;
import com.homework.social_network.repo.AccountRepository;
import com.homework.social_network.service.message.PrivateMessageService;
import com.homework.social_network.service.notification.NotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PrivateMessageService privateMessageService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private NotificationService notificationService;

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
        currentStatusDto.setCurrentAccount(modelMapper.map(account, AccountPageResponseDto.class));
        currentStatusDto.setAccesses(accesses);
        currentStatusDto.setUnacceptedMessages(privateMessageService.getUnacceptedMessages());
        currentStatusDto.setUnacceptedNotifications(notificationService.getUnacceptedNotifications());
        return currentStatusDto;
    }
}
