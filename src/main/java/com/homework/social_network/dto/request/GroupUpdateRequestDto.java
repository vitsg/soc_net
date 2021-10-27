package com.homework.social_network.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class GroupUpdateRequestDto {
    private UUID id;
    private String name;
    private String theme;
    private String ownTheme;
    private String subTheme;
    private String description;
    private String photoName;
    private MultipartFile photo;
}
