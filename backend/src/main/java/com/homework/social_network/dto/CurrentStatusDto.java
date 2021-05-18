package com.homework.social_network.dto;

import com.homework.social_network.enums.Access;
import lombok.Data;

import java.util.List;

@Data
public class CurrentStatusDto {

    private AccountDto currentAccount;
    private List<Access> accesses;
}
