package com.homework.social_network.dto.response.account;

import lombok.Data;

@Data
public class AccountPageInfoAccessResponseDto {

    private boolean canChangePhoto;
    private boolean canRemovePhoto;
    private boolean canModify;
}
