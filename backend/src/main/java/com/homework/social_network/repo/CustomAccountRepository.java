package com.homework.social_network.repo;

import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.Group;

import java.util.List;

public interface CustomAccountRepository {

    List<Account> getAllAccounts(int start, int limit);
}
