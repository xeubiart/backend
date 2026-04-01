package xeubiart.com.domain.identity.providers.local.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xeubiart.com.domain.identity.model.Identity;
import xeubiart.com.domain.identity.providers.local.entity.VerificationLocal;
import xeubiart.com.domain.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VerificationLocalRepository extends JpaRepository<VerificationLocal, UUID> {
    Optional<VerificationLocal> findByIdAndCode(UUID uuid, String code);
    Optional<VerificationLocal> findById(UUID id);

    @Modifying
    void deleteAllByExpiryBefore(LocalDateTime expiry);
}
