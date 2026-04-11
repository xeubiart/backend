package com.xeubiart.verification.entity;

import com.xeubiart.identity.model.IdentityType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.UUID;

@RedisHash("verification_session")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class VerificationSession {
    @Id
    private String sessionToken;
    private String code;

    private UUID accountId;
    private IdentityType provider;

    @TimeToLive
    @Builder.Default
    private Long ttl = 900L;

    @Builder.Default
    private int attempts = 0;

    public void registerWrongAttempt(){
        this.attempts++;
    }

    public boolean isAttemptsExceeded(){
        return this.attempts >= 3;
    }

    // Bugged
    public boolean canSendNewCode(){
        return this.ttl <= 900L - 60L;
    }
}
