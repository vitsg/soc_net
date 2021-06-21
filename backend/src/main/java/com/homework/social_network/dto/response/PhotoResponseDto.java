package com.homework.social_network.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class PhotoResponseDto {

    private UUID id;
    private byte[] data;
}
