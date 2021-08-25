package com.homework.social_network.controller;

import com.homework.social_network.dto.request.message.MessageOnWallRequestDto;
import com.homework.social_network.dto.request.message.MessagesOnWallRequestDto;
import com.homework.social_network.dto.request.message.PrivateMessageRequestDto;
import com.homework.social_network.dto.request.message.PrivateMessagesRequestDto;
import com.homework.social_network.dto.response.message.MessageOnWallResponseDto;
import com.homework.social_network.dto.response.message.MessagesOnWallResponseDto;
import com.homework.social_network.dto.response.message.PrivateMessageResponseDto;
import com.homework.social_network.dto.response.message.PrivateMessagesResponseDto;
import com.homework.social_network.service.message.MessageOnWallService;
import com.homework.social_network.service.message.PrivateMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/message")
@Slf4j
public class MessageController {

    @Autowired
    private PrivateMessageService privateMessageService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageOnWallService messageOnWallService;

    @PostMapping(value = "/get-private-messages")
    public PrivateMessagesResponseDto getPrivateMessages(@RequestBody PrivateMessagesRequestDto requestDto) {
        return privateMessageService.getPrivateMessages(requestDto.getTargetAccountId(), requestDto.getStartLoadTime(),
                requestDto.getStart(), requestDto.getEnd());
    }

    @PostMapping(value = "/acceptMessages")
    public void acceptMessages(@RequestBody List<UUID> messageIds) {
        privateMessageService.acceptMessages(messageIds);
    }

    @PostMapping("/sendMessage")
    public PrivateMessageResponseDto sendMessage(@RequestBody PrivateMessageRequestDto requestDto) {
        PrivateMessageResponseDto responseDto = privateMessageService.save(requestDto);
        simpMessagingTemplate.convertAndSend("/queue/privateMessage/" + requestDto.getDestinationId(), responseDto);
        return responseDto;
    }
    
    @GetMapping("/get-unaccepted-messages")
    public List<PrivateMessageResponseDto> getUnacceptedMessages() {
        return privateMessageService.getUnacceptedMessages();
    }

    @PostMapping("/sendMessageOnWall")
    public MessageOnWallResponseDto sendMessageOnWall(@RequestBody MessageOnWallRequestDto requestDto) {
        MessageOnWallResponseDto responseDto = messageOnWallService.save(requestDto);
        simpMessagingTemplate.convertAndSend("/topic/messageOnWall/" + requestDto.getDestinationId(), responseDto);
        return responseDto;
    }

    @PostMapping(value = "/getMessagesOnWall")
    public MessagesOnWallResponseDto getMessagesOnWall(@RequestBody MessagesOnWallRequestDto requestDto) {
        return messageOnWallService.getMessagesOnWall(requestDto.getTargetId(), requestDto.getStartLoadTime(),
                requestDto.getStart(), requestDto.getEnd());
    }
}
