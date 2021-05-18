package com.homework.social_network.controller;

import com.homework.social_network.dto.AccountDto;
import com.homework.social_network.entity.Account;
import com.homework.social_network.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/registration")
    public AccountDto registration(@RequestBody Account account) {
        return accountService.registration(account);
    }

    @GetMapping("/{id}")
    public Account findOne(@PathVariable String id) {
        return accountService.findOne(id);
    }
}
