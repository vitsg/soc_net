package com.homework.social_network.dto.request.message;

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
public class PrivateMessagesRequestDto {

    private UUID targetAccountId;
    private LocalDateTime startLoadTime;
    private int start;
    private int end;
}
