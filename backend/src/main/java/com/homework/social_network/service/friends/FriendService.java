package com.homework.social_network.service.friends;

import com.homework.social_network.dto.response.account.AccountWithPhotoPageResponseDto;
import com.homework.social_network.dto.response.account.info.AccountMetainfoResponseDto;
import com.homework.social_network.dto.response.account.list.AccountEntryResponseDto;
import com.homework.social_network.dto.response.account.list.AccountSimpleResponseDto;
import com.homework.social_network.dto.response.account.list.AccountsResponseDto;
import com.homework.social_network.entity.Account;
import com.homework.social_network.enums.FriendRelationStatus;
import com.homework.social_network.service.AccountService;
import com.homework.social_network.service.FriendRequestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.homework.social_network.utils.Utils.getOrDefaultContentPhoto;

@Service
public class FriendService {

    @Autowired
    private FriendRequestService friendRequestService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public FriendRelationStatus addFriend(UUID targetId) {
        Account target = accountService.getAccount(targetId);
        return friendRequestService.addFriendRequest(target);
    }

    @Transactional
    public FriendRelationStatus removeFriend(UUID targetId) {
        return friendRequestService.removeFriendRequest(targetId);
    }

    @Transactional
    public FriendRelationStatus rejectFriend(UUID targetId) {
        return friendRequestService.rejectFriendRequest(targetId);
    }

    @Transactional
    public AccountsResponseDto getFriends(UUID targetAccountId, int start, int end, String name) {
        List<UUID> accountIds = friendRequestService.getFriendIds(targetAccountId, start, end);
        Map<UUID, Account> batch = accountService.getBatch(accountIds);
        List<AccountEntryResponseDto> accounts = accountIds.stream()
                .map(id -> {
                    Account account = batch.get(id);
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
                .totalSize(friendRequestService.countFriends(targetAccountId))
                .build();
    }

    @Transactional
    public AccountsResponseDto getInputFriends(UUID accountId, int start, int end) {
        List<UUID> groupIds = friendRequestService.getInputFriends(accountId, start, end);
        Map<UUID, Account> batch = accountService.getBatch(groupIds);
        List<AccountEntryResponseDto> accounts = groupIds.stream()
                .map(id -> {
                    Account account = batch.get(id);
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
                .totalSize(friendRequestService.countInputFriends(accountId))
                .build();
    }

    @Transactional
    public AccountsResponseDto getOutputFriends(UUID accountId, int start, int end) {
        List<UUID> groupIds = friendRequestService.getOutputFriendIds(accountId, start, end);
        Map<UUID, Account> batch = accountService.getBatch(groupIds);
        List<AccountEntryResponseDto> accounts = groupIds.stream()
                .map(id -> {
                    Account account = batch.get(id);
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
                .totalSize(friendRequestService.countOutputFriends(accountId))
                .build();
    }
}
