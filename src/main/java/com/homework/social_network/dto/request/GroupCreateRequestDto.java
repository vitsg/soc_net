package com.homework.social_network.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class GroupCreateRequestDto {

    private String name;
    private String theme;
    private String ownTheme;
    private String subTheme;
    private String description;
    private String photoName;
    private MultipartFile photo;
}
