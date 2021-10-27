package com.homework.social_network.dto.response.message;

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
public class PrivateMessageResponseDto {

    private UUID id;
    private UUID sourceId;
    private UUID destinationId;
    private String content;
    private LocalDateTime sendDate;
    private LocalDateTime createDate;
    private boolean accepted;
}
