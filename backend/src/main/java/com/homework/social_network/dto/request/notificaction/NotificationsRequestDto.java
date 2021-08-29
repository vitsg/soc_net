package com.homework.social_network.dto.request.notificaction;

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
public class NotificationsRequestDto {

    private UUID targetId;
    private LocalDateTime startLoadTime;
    private int start;
    private int end;
}
