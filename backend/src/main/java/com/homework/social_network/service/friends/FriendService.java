package com.homework.social_network.service.friends;

import com.homework.social_network.dto.response.account.info.AccountMetainfoResponseDto;
import com.homework.social_network.dto.response.account.list.AccountEntryResponseDto;
import com.homework.social_network.dto.response.account.list.AccountSimpleResponseDto;
import com.homework.social_network.dto.response.account.list.AccountsResponseDto;
import com.homework.social_network.enums.FriendRelationStatus;
import com.homework.social_network.enums.NotificationType;
import com.homework.social_network.repo.AccountRepository;
import com.homework.social_network.service.AccountService;
import com.homework.social_network.service.notification.NotificationService;
import com.homework.social_network.service.request.FriendRequestService;
import com.homework.social_network.specification.account.friend.FriendSpecification;
import com.homework.social_network.specification.account.friend.InputFriendSpecification;
import com.homework.social_network.specification.account.friend.OutputFriendSpecification;
import com.homework.social_network.utils.CustomPageable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.homework.social_network.utils.Utils.getCurrentAccountId;
import static com.homework.social_network.utils.Utils.getOrDefaultContentPhoto;

@Service
public class FriendService {

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public FriendRelationStatus addFriend(UUID targetId) {
        FriendRelationStatus resultRelation = friendRequestService.addFriendRequest(targetId);
        notificationService.createNotificationAddFriend(getCurrentAccountId(), targetId, resultRelation);
        return resultRelation;
    }

    @Transactional
    public FriendRelationStatus removeFriend(UUID targetId) {
        FriendRelationStatus resultRelation = friendRequestService.removeFriendRequest(targetId);
        notificationService.createNotificationRemoveFriend(getCurrentAccountId(), targetId, resultRelation);
        return resultRelation;
    }

    @Transactional
    public FriendRelationStatus rejectFriend(UUID targetId) {
        FriendRelationStatus resultRelation = friendRequestService.rejectFriendRequest(targetId);
        notificationService.createNotificationRejectFriend(getCurrentAccountId(), targetId);
        return resultRelation;
    }

    @Transactional
    public AccountsResponseDto getFriends(UUID targetAccountId, int start, int end, String name) {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(Sort.Order.asc("lastName"));
        orders.add(Sort.Order.asc("firstName"));
        Sort sort = Sort.by(orders);
        Pageable pageable = new CustomPageable(start, end - start, sort);
        List<AccountEntryResponseDto> accounts = accountRepository.findAll(FriendSpecification.friendSpecification(targetAccountId, name), pageable)
                .stream()
                .map(account -> {
                    AccountSimpleResponseDto simple = modelMapper.map(account, AccountSimpleResponseDto.class);
                    simple.setPhoto(getOrDefaultContentPhoto(account.getPhoto()));
                    AccountMetainfoResponseDto info = AccountMetainfoResponseDto.builder()
                            .friendRelationStatus(FriendRelationStatus.FULL)
                            .build();
                    return AccountEntryResponseDto.builder()
                            .account(simple)
                            .info(info)
                            .build();
                })
                .collect(Collectors.toList());
        return AccountsResponseDto.builder()
                .accounts(accounts)
                .totalSize((int) accountRepository.count(FriendSpecification.countFriendSpecification(targetAccountId, name)))
                .build();
    }

    @Transactional
    public AccountsResponseDto getInputFriends(UUID accountId, int start, int end, String name) {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(Sort.Order.asc("lastName"));
        orders.add(Sort.Order.asc("firstName"));
        Sort sort = Sort.by(orders);
        Pageable pageable = new CustomPageable(start, end - start, sort);
        List<AccountEntryResponseDto> accounts = accountRepository.findAll(InputFriendSpecification.inputFriendSpecification(accountId, name), pageable)
                .stream()
                .map(account -> {
                    AccountSimpleResponseDto simple = modelMapper.map(account, AccountSimpleResponseDto.class);
                    simple.setPhoto(getOrDefaultContentPhoto(account.getPhoto()));
                    AccountMetainfoResponseDto info = AccountMetainfoResponseDto.builder()
                            .friendRelationStatus(FriendRelationStatus.INPUT)
                            .build();
                    return AccountEntryResponseDto.builder()
                            .account(simple)
                            .info(info)
                            .build();
                })
                .collect(Collectors.toList());
        return AccountsResponseDto.builder()
                .accounts(accounts)
                .totalSize((int) accountRepository.count(InputFriendSpecification.countInputFriendSpecification(accountId, name)))
                .build();
    }

    @Transactional
    public AccountsResponseDto getOutputFriends(UUID accountId, int start, int end, String name) {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(Sort.Order.asc("lastName"));
        orders.add(Sort.Order.asc("firstName"));
        Sort sort = Sort.by(orders);
        Pageable pageable = new CustomPageable(start, end - start, sort);
        List<AccountEntryResponseDto> accounts = accountRepository.findAll(OutputFriendSpecification.outputFriendSpecification(accountId, name), pageable)
                .stream()
                .map(account -> {
                    AccountSimpleResponseDto simple = modelMapper.map(account, AccountSimpleResponseDto.class);
                    simple.setPhoto(getOrDefaultContentPhoto(account.getPhoto()));
                    AccountMetainfoResponseDto info = AccountMetainfoResponseDto.builder()
                            .friendRelationStatus(FriendRelationStatus.OUTPUT)
                            .build();
                    return AccountEntryResponseDto.builder()
                            .account(simple)
                            .info(info)
                            .build();
                })
                .collect(Collectors.toList());
        return AccountsResponseDto.builder()
                .accounts(accounts)
                .totalSize((int) accountRepository.count(OutputFriendSpecification.countOutputFriendSpecification(accountId, name)))
                .build();
    }

}
