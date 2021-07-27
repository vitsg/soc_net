package com.homework.social_network.repo;

import com.homework.social_network.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID>, CustomAccountRepository, JpaSpecificationExecutor<Account> {

    Account findByEmail(String email);

    Account getById(UUID id);
}
