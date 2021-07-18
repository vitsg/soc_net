package com.homework.social_network.dto.response.account;

import com.homework.social_network.dto.response.account.page.AccountPageResponseDto;
import lombok.Data;

@Data
public class AccountWithPhotoPageResponseDto extends AccountPageResponseDto {
    private byte[] photo;
}
