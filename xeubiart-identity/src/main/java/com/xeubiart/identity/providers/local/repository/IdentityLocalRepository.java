package com.xeubiart.identity.providers.local.repository;

import com.xeubiart.identity.providers.local.entity.IdentityLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IdentityLocalRepository extends JpaRepository<IdentityLocal, UUID> {
    boolean existsByAccountId(UUID accountId);
    Optional<IdentityLocal> findByAccountId(UUID accountId);
}
