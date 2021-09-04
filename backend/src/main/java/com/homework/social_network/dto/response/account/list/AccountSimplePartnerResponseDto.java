package com.homework.social_network.dto.response.account.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountSimplePartnerResponseDto extends AccountSimpleResponseDto {

    private boolean accepted;
}
