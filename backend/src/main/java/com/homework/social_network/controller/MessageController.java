package com.homework.social_network.controller;

import com.homework.social_network.dto.request.PrivateMessageRequestDto;
import com.homework.social_network.dto.response.message.PrivateMessagesResponseDto;
import com.homework.social_network.service.message.PrivateMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private PrivateMessageService privateMessageService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping(value = "/get-private-messages")
    public PrivateMessagesResponseDto getPrivateMessages(@RequestParam UUID targetAccountId,
                                                         @RequestParam(required = false) LocalDateTime startLoadTime,
                                                         @RequestParam int start,
                                                         @RequestParam int end) {
        return privateMessageService.getPrivateMessages(targetAccountId, startLoadTime, start, end);
    }

    @PostMapping(value = "/acceptMessages")
    public void acceptMessages(List<UUID> messageIds) {
        privateMessageService.acceptMessages(messageIds);
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(PrivateMessageRequestDto requestDto) {
        simpMessagingTemplate.convertAndSend("/queue/privateMessage/" + requestDto.getDestinationId(), privateMessageService.save(requestDto));
    }
}
