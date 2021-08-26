package com.homework.social_network.dto.response.message;

import com.homework.social_network.dto.response.account.list.AccountSimpleResponseDto;
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
public class MessageOnWallFullResponseDto {

    private UUID id;
    private AccountSimpleResponseDto source;
    private UUID destinationId;
    private String content;
    private MessageOnWallType messageOnWallType;
    private LocalDateTime sendDate;
    private LocalDateTime createDate;
}
