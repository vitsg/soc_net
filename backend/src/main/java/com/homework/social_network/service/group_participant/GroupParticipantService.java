package com.homework.social_network.service.group_participant;

import com.homework.social_network.dto.response.account.info.AccountMetainfoResponseDto;
import com.homework.social_network.dto.response.account.list.AccountEntryResponseDto;
import com.homework.social_network.dto.response.account.list.AccountSimpleResponseDto;
import com.homework.social_network.dto.response.account.list.AccountsResponseDto;
import com.homework.social_network.dto.response.group.info.GroupMetainfoResponseDto;
import com.homework.social_network.dto.response.group.list.GroupEntryResponseDto;
import com.homework.social_network.dto.response.group.list.GroupSimpleResponseDto;
import com.homework.social_network.dto.response.group.list.GroupsResponseDto;
import com.homework.social_network.entity.Group;
import com.homework.social_network.entity.GroupJoin;
import com.homework.social_network.enums.GroupRelationStatus;
import com.homework.social_network.repo.AccountRepository;
import com.homework.social_network.repo.GroupRepository;
import com.homework.social_network.service.relation.AccountRelationService;
import com.homework.social_network.service.AccountService;
import com.homework.social_network.service.request.GroupRequestService;
import com.homework.social_network.specification.account.GroupParticipantSpecification;
import com.homework.social_network.specification.group.JoinGroupSpecification;
import com.homework.social_network.utils.CustomPageable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.homework.social_network.utils.Utils.getCurrentAccountId;
import static com.homework.social_network.utils.Utils.getOrDefaultContentPhoto;

@Service
public class GroupParticipantService {

    @Autowired
    private GroupRequestService groupRequestService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountRelationService accountRelationService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public GroupRelationStatus joinGroup(UUID targetId) {
        UUID currentAccountId = getCurrentAccountId();
        return groupRequestService.joinGroup(currentAccountId, targetId, GroupRelationStatus.PARTICIPANT);
    }

    @Transactional
    public GroupRelationStatus leaveGroup(UUID targetId) {
        return groupRequestService.leaveGroup(targetId);
    }

    @Transactional
    public GroupsResponseDto getAccountGroups(UUID accountId, int start, int end, String name) {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        Pageable pageable = new CustomPageable(start, end - start, sort);
        List<Group> groups = groupRepository.findAll(JoinGroupSpecification.joinGroupSpecification(accountId, name), pageable).toList();
        Map<UUID, GroupRelationStatus> statuses = groupRequestService.getRequestGroups(accountId, groups.stream().map(Group::getId).collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(GroupJoin::getTargetGroupId, GroupJoin::getStatus));
        List<GroupEntryResponseDto> groupEntry = groups.stream()
                .map(group -> {
                    GroupSimpleResponseDto simple = modelMapper.map(group, GroupSimpleResponseDto.class);
                    simple.setPhoto(getOrDefaultContentPhoto(group.getPhoto()));
                    GroupMetainfoResponseDto info = GroupMetainfoResponseDto.builder()
                            .groupRelationStatus(statuses.get(group.getId()))
                            .build();
                    return GroupEntryResponseDto.builder()
                            .group(simple)
                            .info(info)
                            .build();
                })
                .collect(Collectors.toList());
        return GroupsResponseDto.builder()
                .groups(groupEntry)
                .totalSize((int) groupRepository.count(JoinGroupSpecification.countJoinGroupSpecification(accountId, name)))
                .build();
    }

    @Transactional
    public AccountsResponseDto getGroupAccounts(UUID groupId, int start, int end, String name) {
        UUID currentAccountId = getCurrentAccountId();
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(Sort.Order.asc("lastName"));
        orders.add(Sort.Order.asc("firstName"));
        Sort sort = Sort.by(orders);
        Pageable pageable = new CustomPageable(start, end - start, sort);
        List<AccountEntryResponseDto> accounts = accountRepository.findAll(GroupParticipantSpecification.groupParticipantSpecification(groupId, name), pageable)
                .stream()
                .map(account -> {
                    AccountSimpleResponseDto simple = modelMapper.map(account, AccountSimpleResponseDto.class);
                    simple.setPhoto(getOrDefaultContentPhoto(account.getPhoto()));
                    AccountMetainfoResponseDto info = AccountMetainfoResponseDto.builder()
                            .friendRelationStatus(accountRelationService.getRelationAccounts(currentAccountId, account.getId()))
                            .build();
                    return AccountEntryResponseDto.builder()
                            .account(simple)
                            .info(info)
                            .build();
                })
                .collect(Collectors.toList());
        return AccountsResponseDto.builder()
                .accounts(accounts)
                .totalSize((int) accountRepository.count(GroupParticipantSpecification.countGroupParticipantSpecification(groupId, name)))
                .build();
    }
}
