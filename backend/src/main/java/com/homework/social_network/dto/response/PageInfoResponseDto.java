package com.homework.social_network.dto.response;

import lombok.Data;

@Data
public class PageInfoResponseDto {

    private AccountResponseDto account;
    private PageInfoAccessResponseDto accesses;
}
