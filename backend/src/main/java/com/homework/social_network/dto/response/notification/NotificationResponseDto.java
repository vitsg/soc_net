package com.homework.social_network.dto.response.notification;

import com.homework.social_network.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDto {

    private UUID id;
    private NotificationType notificationType;
    private UUID sourceId;
    private UUID destinationId;
    private boolean accepted;
    private LocalDateTime createDate;
}
