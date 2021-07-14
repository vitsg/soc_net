package com.homework.social_network.dto.response.account;

import com.homework.social_network.dto.response.account.AccountResponseDto;
import lombok.Data;

@Data
public class AccountWithPhotoResponseDto extends AccountResponseDto {
    private byte[] photo;
}
