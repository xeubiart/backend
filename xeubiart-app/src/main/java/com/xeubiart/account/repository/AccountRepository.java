package com.xeubiart.account.repository;

import com.xeubiart.account.entity.Account;
import com.xeubiart.account.model.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByEmailAndRole(String email, AccountRole role);
    Optional<Account> findByEmail(String email);
    Optional<Account> findById(UUID id);
}
