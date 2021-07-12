package com.homework.social_network.controller;

import com.homework.social_network.dto.response.GroupsResponseDto;
import com.homework.social_network.enums.GroupRelationStatus;
import com.homework.social_network.service.GroupRelationService;
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
public class GroupRelationController {

    @Autowired
    private GroupRelationService groupRelationService;

    @PostMapping("/join-group/{id}")
    public GroupRelationStatus joinGroup(@PathVariable UUID id) {
        return groupRelationService.joinGroup(id);
    }

    @PostMapping("/leave-group/{id}")
    public GroupRelationStatus leaveGroup(@PathVariable UUID id) {
        return groupRelationService.leaveGroup(id);
    }

    @GetMapping("/get-participant-groups/{id}")
    public GroupsResponseDto getParticipantGroups(@PathVariable UUID id, @RequestParam int start, @RequestParam int end) {
        return groupRelationService.getParticipantGroups(id, start, end);
    }
}
