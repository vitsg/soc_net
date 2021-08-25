package com.homework.social_network.dto.request.message;

import com.homework.social_network.enums.MessageOnWallType;
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
public class MessageOnWallRequestDto {

    private UUID sourceId;
    private UUID destinationId;
    private String content;
    private MessageOnWallType messageOnWallType;
    private LocalDateTime sendDate;
}
