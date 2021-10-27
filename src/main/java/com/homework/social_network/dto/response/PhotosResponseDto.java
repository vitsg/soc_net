package com.homework.social_network.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PhotosResponseDto {

    private List<PhotoResponseDto> photos;
    private int totalSize;
}
