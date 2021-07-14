package com.homework.social_network.controller;

import com.homework.social_network.dto.response.account.AccountsResponseDto;
import com.homework.social_network.enums.FriendRelationStatus;
import com.homework.social_network.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/friend")
public class FriendController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/addFriend/{id}")
    public FriendRelationStatus addFriend(@PathVariable UUID id) {
        return accountService.addFriend(id);
    }

    @PostMapping("/removeFriend/{id}")
    public FriendRelationStatus removeFriend(@PathVariable UUID id) {
        return accountService.removeFriend(id);
    }

    @GetMapping("/get-friends/{id}")
    public AccountsResponseDto getFriends(@PathVariable UUID id, @RequestParam int start, @RequestParam int end) {
        return accountService.getFriends(id, start, end);
    }

    @GetMapping("/get-input-friends/{id}")
    public AccountsResponseDto getInputFriends(@PathVariable UUID id, @RequestParam int start, @RequestParam int end) {
        return accountService.getInputFriends(id, start, end);
    }

    @GetMapping("/get-output-friends/{id}")
    public AccountsResponseDto getOutputFriends(@PathVariable UUID id, @RequestParam int start, @RequestParam int end) {
        return accountService.getOutputFriends(id, start, end);
    }
}
