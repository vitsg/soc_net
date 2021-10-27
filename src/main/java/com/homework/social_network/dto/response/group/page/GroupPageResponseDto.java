package com.homework.social_network.dto.response.group.page;

import com.homework.social_network.dto.response.account.AccountGroupOwnerResponseDto;
import lombok.Data;

import java.util.UUID;

@Data
public class GroupPageResponseDto {

    private UUID id;
    private String name;
    private String theme;
    private String ownTheme;
    private String subTheme;
    private String description;
    private String photoName;
    private AccountGroupOwnerResponseDto owner;
}
