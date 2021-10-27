package com.homework.social_network.repo;

import com.homework.social_network.entity.Account;
import com.homework.social_network.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID>, CustomAccountRepository, JpaSpecificationExecutor<Account> {

    Account findByEmail(String email);

    Account getById(UUID id);

    @Query("select a from Account a left join fetch a.photo where a.id = :id")
    Account getByIdWithPhoto(UUID id);

    boolean existsByIdAndPartnerId(UUID id, UUID partnerId);
}
