package com.homework.social_network.dto.response.message;

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
public class PrivateMessagesResponseDto {

    private List<PrivateMessageResponseDto> messages;
    private LocalDateTime startLoadTime;
}
