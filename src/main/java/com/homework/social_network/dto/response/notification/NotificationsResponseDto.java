package com.homework.social_network.dto.response.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationsResponseDto {

    private List<NotificationFullResponseDto> notifications;
    private long totalSize;
    private LocalDateTime startLoadTime;
}
