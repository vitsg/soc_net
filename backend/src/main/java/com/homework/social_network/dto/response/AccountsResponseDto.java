package com.homework.social_network.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class AccountsResponseDto {

    private List<AccountWithPhotoResponseDto> accounts;
    private int totalSize;
}
