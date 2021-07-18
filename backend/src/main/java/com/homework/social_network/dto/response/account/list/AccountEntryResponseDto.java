package com.homework.social_network.dto.response.account.list;

import com.homework.social_network.dto.response.account.info.AccountMetainfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountEntryResponseDto {

    private AccountSimpleResponseDto account;
    private AccountMetainfoResponseDto info;
}
