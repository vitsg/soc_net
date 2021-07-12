package com.homework.social_network.service;

import com.homework.social_network.dto.response.AccountWithPhotoResponseDto;
import com.homework.social_network.dto.response.AccountsResponseDto;
import com.homework.social_network.dto.response.GroupWithPhotoResponseDto;
import com.homework.social_network.dto.response.GroupsResponseDto;
import com.homework.social_network.entity.FriendRequest;
import com.homework.social_network.enums.FriendRelationStatus;
import com.homework.social_network.repo.FriendRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.homework.social_network.utils.Utils.getCurrentAccountId;

@Service
public class FriendRelationService {

    @Autowired
    private FriendRequestService friendRequestService;
    @Autowired
    private AccountService accountService;

    @Transactional
    public FriendRelationStatus addFriend(UUID targetId) {
        return friendRequestService.addFriend(targetId);
    }

    @Transactional
    public FriendRelationStatus removeFriend(UUID targetId) {
        return friendRequestService.removeFriend(targetId);
    }

    @Transactional
    public AccountsResponseDto getFriends(UUID accountId, int start, int end) {
        List<UUID> groupIds = friendRequestService.getFriendIds(accountId, start, end);
        Map<UUID, AccountWithPhotoResponseDto> batch = accountService.getBatch(groupIds);
        List<AccountWithPhotoResponseDto> groups = groupIds.stream()
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
        Map<UUID, AccountWithPhotoResponseDto> batch = accountService.getBatch(groupIds);
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
        Map<UUID, AccountWithPhotoResponseDto> batch = accountService.getBatch(groupIds);
        List<AccountWithPhotoResponseDto> groups = groupIds.stream()
                .map(batch::get)
                .collect(Collectors.toList());
        AccountsResponseDto groupsResponseDto = new AccountsResponseDto();
        groupsResponseDto.setAccounts(groups);
        groupsResponseDto.setTotalSize(friendRequestService.countOutputFriends(accountId));
        return groupsResponseDto;
    }
}
