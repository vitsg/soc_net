package com.homework.social_network.dto.response.account.access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountAccessResponseDto {

    private boolean canChangePhoto;
    private boolean canRemovePhoto;
    private boolean canModify;
}
