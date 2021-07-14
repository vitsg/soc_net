package com.homework.social_network.dto.response.account;

import lombok.Data;

@Data
public class AccountPageInfoResponseDto {

    private AccountResponseDto account;
    private AccountPageInfoAccessResponseDto accesses;
    private AccountPageMetainfoResponseDto info;
}
