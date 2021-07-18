package com.homework.social_network.service.group_participant;

import com.homework.social_network.dto.response.account.info.AccountMetainfoResponseDto;
import com.homework.social_network.dto.response.account.list.AccountEntryResponseDto;
import com.homework.social_network.dto.response.account.list.AccountSimpleResponseDto;
import com.homework.social_network.dto.response.account.list.AccountsResponseDto;
import com.homework.social_network.dto.response.group.info.GroupMetainfoResponseDto;
import com.homework.social_network.dto.response.group.list.GroupEntryResponseDto;
import com.homework.social_network.dto.response.group.list.GroupSimpleResponseDto;
import com.homework.social_network.dto.response.group.list.GroupsResponseDto;
import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.Group;
import com.homework.social_network.entity.GroupJoin;
import com.homework.social_network.enums.GroupRelationStatus;
import com.homework.social_network.service.AccountRelationService;
import com.homework.social_network.service.AccountService;
import com.homework.social_network.service.GroupRequestService;
import com.homework.social_network.service.GroupService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.homework.social_network.utils.Utils.getCurrentAccount;
import static com.homework.social_network.utils.Utils.getOrDefaultContentPhoto;

@Service
public class GroupParticipantService {

    @Autowired
    private GroupRequestService groupRequestService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountRelationService accountRelationService;

    @Transactional
    public GroupRelationStatus joinGroup(UUID targetId) {
        Group targetGroup = groupService.getGroup(targetId);
        return groupRequestService.joinGroup(targetGroup);
    }

    @Transactional
    public GroupRelationStatus leaveGroup(UUID targetId) {
        return groupRequestService.leaveGroup(targetId);
    }

    @Transactional
    public GroupsResponseDto getAccountGroups(UUID accountId, int start, int end) {
        List<GroupJoin> groupJoins = groupRequestService.getAccountGroupIds(accountId, start, end);
        Map<UUID, Group> batch = groupService.getBatch(groupJoins.stream()
                .map(gr -> gr.getTargetGroup().getId())
                .collect(Collectors.toList()));
        List<GroupEntryResponseDto> groups = groupJoins.stream()
                .map(join -> {
                    Group group = batch.get(join.getTargetGroup().getId());
                    GroupSimpleResponseDto simple = modelMapper.map(group, GroupSimpleResponseDto.class);
                    simple.setPhoto(getOrDefaultContentPhoto(group.getPhoto()));
                    GroupMetainfoResponseDto info = GroupMetainfoResponseDto.builder()
                            .groupRelationStatus(join.getStatus())
                            .build();
                    return GroupEntryResponseDto.builder()
                            .group(simple)
                            .info(info)
                            .build();
                })
                .collect(Collectors.toList());
        return GroupsResponseDto.builder()
                .groups(groups)
                .totalSize(groupRequestService.countAccountGroups(accountId))
                .build();
    }

    @Transactional
    public AccountsResponseDto getGroupAccounts(UUID groupId, int start, int end) {
        Account currentAccount = getCurrentAccount();
        List<GroupJoin> groupJoins = groupRequestService.getAccountGroupIds(groupId, start, end);
        Map<UUID, Account> batch = accountService.getBatch(groupJoins.stream()
                .map(gr -> gr.getOwnerAccount().getId())
                .collect(Collectors.toList()));
        List<AccountEntryResponseDto> accounts = groupJoins.stream()
                .map(join -> {
                    Account account = batch.get(join.getOwnerAccount().getId());
                    AccountSimpleResponseDto simple = modelMapper.map(account, AccountSimpleResponseDto.class);
                    simple.setPhoto(getOrDefaultContentPhoto(account.getPhoto()));
                    AccountMetainfoResponseDto info = AccountMetainfoResponseDto.builder()
                            .friendRelationStatus(accountRelationService.getRelationAccounts(currentAccount, account))
                            .build();
                    return AccountEntryResponseDto.builder()
                            .account(simple)
                            .info(info)
                            .build();
                })
                .collect(Collectors.toList());
        return AccountsResponseDto.builder()
                .accounts(accounts)
                .totalSize(groupRequestService.countGroupAccounts(groupId))
                .build();
    }
}
