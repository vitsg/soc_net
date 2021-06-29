package com.homework.social_network.controller;

import com.homework.social_network.dto.request.GroupCreateRequestDto;
import com.homework.social_network.dto.request.GroupUpdateRequestDto;
import com.homework.social_network.dto.request.PhotoRequestDto;
import com.homework.social_network.dto.response.GroupPageInfoResponseDto;
import com.homework.social_network.dto.response.GroupResponseDto;
import com.homework.social_network.dto.response.AccountPageInfoResponseDto;
import com.homework.social_network.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/create")
    public GroupResponseDto create(@ModelAttribute GroupCreateRequestDto groupCreateRequestDto) {
        return groupService.create(groupCreateRequestDto);
    }

    @GetMapping("/{id}")
    public GroupResponseDto findOne(@PathVariable String id) {
        return groupService.findOne(id);
    }

    @PostMapping
    public GroupResponseDto update(@RequestBody GroupUpdateRequestDto groupUpdateRequestDto) {
        return groupService.update(groupUpdateRequestDto);
    }

    @GetMapping("/{id}/photo")
    public byte[] getGroupPhoto(@PathVariable UUID id) {
        return groupService.getGroupPhoto(id);
    }

    @GetMapping("/{id}/page-info")
    public GroupPageInfoResponseDto getPageInfo(@PathVariable UUID id) {
        return groupService.getPageInfo(id);
    }
}
