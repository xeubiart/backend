package xeubiart.com.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xeubiart.com.domain.identity.providers.local.entity.IdentityLocal;
import xeubiart.com.domain.identity.providers.local.repository.IdentityLocalRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CleanupService {
    private IdentityLocalRepository identityLocalRepository;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanUnverifiedExpiredAccounts() {
        LocalDateTime now = LocalDateTime.now();

        // 1. Find the identities that are NOT active and have an EXPIRED verification
        List<IdentityLocal> abandonedIdentities = identityLocalRepository.findAllByActiveFalseAndVerificationExpiryBefore(now);

        // It deletes: User, IdentityLocal, and VerificationLocal
        this.identityLocalRepository.deleteAll(abandonedIdentities);
    }

}
