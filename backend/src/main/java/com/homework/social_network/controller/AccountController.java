package com.homework.social_network.controller;

import com.homework.social_network.dto.request.AccountRegistrationRequestDto;
import com.homework.social_network.dto.request.AccountUpdateRequestDto;
import com.homework.social_network.dto.request.PhotoRequestDto;
import com.homework.social_network.dto.response.AccountResponseDto;
import com.homework.social_network.dto.response.AccountPageInfoResponseDto;
import com.homework.social_network.service.AccountService;
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
@RequestMapping("api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/registration")
    public AccountResponseDto registration(@RequestBody AccountRegistrationRequestDto accountRegistrationRequestDto) {
        return accountService.registration(accountRegistrationRequestDto);
    }

    @GetMapping("/{id}")
    public AccountResponseDto findOne(@PathVariable String id) {
        return accountService.findOne(id, true);
    }

    @PostMapping
    public AccountResponseDto update(@RequestBody AccountUpdateRequestDto accountUpdateRequestDto) {
        return accountService.update(accountUpdateRequestDto);
    }

    @PostMapping("/{id}/change-photo")
    public AccountPageInfoResponseDto changeDefaultPhoto(@PathVariable UUID id, @ModelAttribute PhotoRequestDto photoRequestDto) {
        return accountService.changePhoto(id, photoRequestDto);
    }

    @GetMapping("/{id}/photo")
    public byte[] getAccountPhoto(@PathVariable UUID id) {
        return accountService.getAccountPhoto(id);
    }

    @GetMapping("/{id}/page-info")
    public AccountPageInfoResponseDto getPageInfo(@PathVariable UUID id) {
        return accountService.getPageInfo(id);
    }
}
