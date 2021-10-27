package com.homework.social_network.repo;

import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.Group;

import java.util.List;
import java.util.UUID;

public interface CustomAccountRepository {

    List<Account> findAllByIds(List<UUID> ids);

}
