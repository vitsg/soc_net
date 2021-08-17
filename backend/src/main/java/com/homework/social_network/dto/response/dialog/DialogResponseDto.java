package com.homework.social_network.dto.response.dialog;

import com.homework.social_network.dto.response.account.list.AccountSimpleResponseDto;
import com.homework.social_network.dto.response.message.PrivateMessageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DialogResponseDto {

    private AccountSimpleResponseDto account;
    private PrivateMessageResponseDto lastMessage;
    private long countUnacceptedMessages;
}
