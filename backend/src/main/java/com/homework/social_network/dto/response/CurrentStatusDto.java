package com.homework.social_network.dto.response;

import com.homework.social_network.dto.response.account.AccountResponseDto;
import com.homework.social_network.enums.Access;
import lombok.Data;

import java.util.List;

@Data
public class CurrentStatusDto {

    private AccountResponseDto currentAccount;
    private List<Access> accesses;
}
