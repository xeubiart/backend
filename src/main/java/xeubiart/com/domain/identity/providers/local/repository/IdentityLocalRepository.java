package xeubiart.com.domain.identity.providers.local.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xeubiart.com.domain.identity.providers.local.entity.IdentityLocal;
import xeubiart.com.domain.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IdentityLocalRepository extends JpaRepository<IdentityLocal, UUID> {
    Optional<IdentityLocal> findByEmail(String email);

    List<IdentityLocal> findAllByActiveFalseAndVerificationExpiryBefore(LocalDateTime expiry);
}
