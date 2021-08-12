package com.homework.social_network.socket;

import com.homework.social_network.dto.request.PrivateMessageRequestDto;
import com.homework.social_network.service.message.PrivateMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageWebSocket {

    @Autowired
    private PrivateMessageService privateMessageService;

//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;
//
//    @MessageMapping("/sendMessage")
//    public void sendMessage(PrivateMessageRequestDto requestDto) {
//        simpMessagingTemplate.convertAndSend("/queue/privateMessage/" + requestDto.getDestinationId(), privateMessageService.save(requestDto));
//    }

//    @MessageMapping("/sendMessageOnAccountWall")
//    public void sendMessageOnAccountWall(MessageOnWall message) {
//        simpMessagingTemplate.convertAndSend("/queue/accountMessagesOnWall/" + message.getDestinationId(), privateMessageService.save(message));
//    }
//
//    @MessageMapping("/sendMessageOnGroupWall")
//    public void sendMessageOnGroupWall(MessageOnWall message) {
//        simpMessagingTemplate.convertAndSend("/queue/groupMessagesOnWall/" + message.getDestinationId(), privateMessageService.save(message));
//    }
}
