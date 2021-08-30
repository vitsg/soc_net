package com.homework.social_network.controller;

import com.homework.social_network.dto.request.AccountRegistrationRequestDto;
import com.homework.social_network.dto.request.AccountUpdateRequestDto;
import com.homework.social_network.dto.request.ChangeCredentialsRequestDto;
import com.homework.social_network.dto.request.PhotoRequestDto;
import com.homework.social_network.dto.response.account.list.AccountSimpleResponseDto;
import com.homework.social_network.dto.response.account.page.AccountPageResponseDto;
import com.homework.social_network.dto.response.account.page.AccountPageInfoResponseDto;
import com.homework.social_network.dto.response.account.list.AccountsResponseDto;
import com.homework.social_network.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/registration")
    public AccountPageResponseDto registration(@RequestBody AccountRegistrationRequestDto accountRegistrationRequestDto) {
        return accountService.registration(accountRegistrationRequestDto);
    }

    @GetMapping("/{id}")
    public AccountPageResponseDto findOne(@PathVariable UUID id) {
        return accountService.findOne(id);
    }

    @PostMapping
    public AccountPageResponseDto update(@RequestBody AccountUpdateRequestDto accountUpdateRequestDto) {
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

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public AccountsResponseDto getAccounts(@RequestParam int start, @RequestParam int end, @RequestParam(required = false) String name) {
        return accountService.getAllAccount(start, end, name, false);
    }

    @RequestMapping(value = "/allWithoutSelf", method = RequestMethod.GET)
    public AccountsResponseDto getAccountsWithoutSelf(@RequestParam int start, @RequestParam int end, @RequestParam(required = false) String name) {
        return accountService.getAllAccount(start, end, name, true);
    }

    @PostMapping("/change-credentials")
    public void changeCredentials(@RequestBody ChangeCredentialsRequestDto changeCredentialsRequestDto) {
        accountService.changeCredentials(changeCredentialsRequestDto);
    }

    @PostMapping("/simple-account/{id}")
    public AccountSimpleResponseDto getSimple(@PathVariable UUID id) {
        return accountService.getSimple(id);
    }

}
