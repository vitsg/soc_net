package com.homework.social_network.dto.response.account;

import com.homework.social_network.enums.FriendRelationStatus;
import lombok.Data;

@Data
public class AccountPageMetainfoResponseDto {

    private FriendRelationStatus friendRelationStatus;
}
