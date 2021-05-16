package com.homework.social_network.controller;

import com.homework.social_network.enums.Access;
import com.homework.social_network.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/status")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping
    public List<Access> status() {
        return authService.getAccessForCurrentUser();
    }
}
