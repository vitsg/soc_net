package com.homework.social_network.controller;

import com.homework.social_network.dto.response.account.list.AccountsResponseDto;
import com.homework.social_network.dto.response.group.list.GroupsResponseDto;
import com.homework.social_network.enums.GroupRelationStatus;
import com.homework.social_network.service.group_participant.GroupParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/group-relation")
public class GroupParticipantController {

    @Autowired
    private GroupParticipantService groupParticipantService;

    @PostMapping("/join-group/{id}")
    public GroupRelationStatus joinGroup(@PathVariable UUID id) {
        return groupParticipantService.joinGroup(id);
    }

    @PostMapping("/leave-group/{id}")
    public GroupRelationStatus leaveGroup(@PathVariable UUID id) {
        return groupParticipantService.leaveGroup(id);
    }

    @GetMapping("/get-account-groups/{id}")
    public GroupsResponseDto getAccountGroups(@PathVariable UUID id, @RequestParam int start, @RequestParam int end, @RequestParam(required = false) String name) {
        return groupParticipantService.getAccountGroups(id, start, end, name);
    }

    @GetMapping("/get-group-accounts/{id}")
    public AccountsResponseDto getGroupAccounts(@PathVariable UUID id, @RequestParam int start, @RequestParam int end, @RequestParam(required = false) String name) {
        return groupParticipantService.getGroupAccounts(id, start, end, name);
    }
}
