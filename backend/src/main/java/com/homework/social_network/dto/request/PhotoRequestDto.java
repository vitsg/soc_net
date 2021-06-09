package com.homework.social_network.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PhotoRequestDto {

    private MultipartFile photo;
}
