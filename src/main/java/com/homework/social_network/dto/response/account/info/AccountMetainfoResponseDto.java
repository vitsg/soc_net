package com.homework.social_network.dto.response.account.info;

import com.homework.social_network.enums.FriendRelationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountMetainfoResponseDto {

    private FriendRelationStatus friendRelationStatus;
}
