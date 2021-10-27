package com.homework.social_network.dto.response.account.page;

import com.homework.social_network.dto.response.account.info.AccountMetainfoResponseDto;
import com.homework.social_network.dto.response.account.access.AccountAccessResponseDto;
import lombok.Data;

@Data
public class AccountPageInfoResponseDto {

    private AccountPageResponseDto account;
    private AccountAccessResponseDto accesses;
    private AccountMetainfoResponseDto info;
}
