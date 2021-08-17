package com.homework.social_network.controller;

import com.homework.social_network.dto.response.dialog.DialogResponseDto;
import com.homework.social_network.dto.response.dialog.DialogsResponseDto;
import com.homework.social_network.service.dialog.DialogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/dialog")
public class DialogController {

    @Autowired
    private DialogService dialogService;

    @GetMapping("/list")
    public DialogsResponseDto getDialogs(@RequestParam int start, @RequestParam int end) {
        return dialogService.getDialogs(start, end);
    }

    @GetMapping("/{targetId}")
    public DialogResponseDto getDialog(@PathVariable UUID targetId) {
        return dialogService.getDialog(targetId);
    }
}
