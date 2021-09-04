package com.homework.social_network.dto.response.account.list;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountSimplePartnerResponseDto extends AccountSimpleResponseDto {

    private boolean accepted;
}
