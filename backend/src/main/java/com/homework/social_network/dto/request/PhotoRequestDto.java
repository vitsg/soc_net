package com.homework.social_network.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class PhotoRequestDto {

    private UUID userId;
    private MultipartFile photo;
}
