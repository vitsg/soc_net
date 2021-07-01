package com.homework.social_network.repo;

import com.homework.social_network.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID>, CustomAccountRepository {

    Account findByEmail(String email);

    Account getById(UUID id);
}
