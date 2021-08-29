package com.homework.social_network.controller;

import com.homework.social_network.dto.request.notificaction.NotificationsRequestDto;
import com.homework.social_network.dto.response.message.PrivateMessageResponseDto;
import com.homework.social_network.dto.response.notification.NotificationResponseDto;
import com.homework.social_network.dto.response.notification.NotificationsResponseDto;
import com.homework.social_network.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/get-user-notifications")
    public NotificationsResponseDto getUserNotifications(@RequestBody NotificationsRequestDto requestDto) {
        return notificationService.getUserNotifications(requestDto.getTargetId(), requestDto.getStartLoadTime(),
                requestDto.getStart(), requestDto.getEnd());
    }

    @PostMapping(value = "/acceptNotifications")
    public void acceptMessages(@RequestBody List<UUID> notificationIds) {
        notificationService.acceptNotifications(notificationIds);
    }

    @GetMapping("/getUnacceptedNotifications")
    public List<NotificationResponseDto> getUnacceptedNotifications() {
        return notificationService.getUnacceptedNotifications();
    }
}
