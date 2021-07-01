package com.homework.social_network.dto.response;

import lombok.Data;

@Data
public class AccountWithPhotoResponseDto extends AccountResponseDto {
    private byte[] photo;
}
