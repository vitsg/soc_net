package com.homework.social_network.controller;

import com.homework.social_network.dto.request.GroupCreateRequestDto;
import com.homework.social_network.dto.request.GroupUpdateRequestDto;
import com.homework.social_network.dto.response.group.page.GroupPageInfoResponseDto;
import com.homework.social_network.dto.response.group.GroupWithPhotoPageResponseDto;
import com.homework.social_network.dto.response.group.list.GroupsResponseDto;
import com.homework.social_network.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/create")
    public GroupWithPhotoPageResponseDto create(@ModelAttribute GroupCreateRequestDto groupCreateRequestDto) {
        return groupService.create(groupCreateRequestDto);
    }

    @GetMapping("/{id}")
    public GroupWithPhotoPageResponseDto findOne(@PathVariable String id) {
        return groupService.findOne(id);
    }

    @PostMapping
    public GroupWithPhotoPageResponseDto update(@ModelAttribute GroupUpdateRequestDto groupUpdateRequestDto) {
        return groupService.update(groupUpdateRequestDto);
    }

    @GetMapping("/{id}/page-info")
    public GroupPageInfoResponseDto getPageInfo(@PathVariable UUID id) {
        return groupService.getPageInfo(id);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public GroupsResponseDto getGroups(@RequestParam int start, @RequestParam int end) {
        return groupService.getAllGroups(start, end);
    }
}
