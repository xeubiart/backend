package com.xeubiart.verification.entity;

import com.xeubiart.identity.model.IdentityType;
import com.xeubiart.verification.exceptions.BadVerificationException;
import com.xeubiart.verification.exceptions.VerificationAttemptsExceededException;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.Instant;
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

    @Builder.Default
    private Instant lastSentAt = Instant.now();

    public boolean canSendNewCode(){
        return this.lastSentAt.plusSeconds(60).isBefore(Instant.now());
    }

    public void changeCode(String code){
        this.code = code;
        this.lastSentAt = Instant.now();
        this.attempts = 0;
    }

    public void validate(String inputCode) throws BadVerificationException, VerificationAttemptsExceededException {
        if (this.attempts >= 3) {
            throw new VerificationAttemptsExceededException();
        }

        if (!this.code.equals(inputCode)) {
            this.attempts++;
            throw new BadVerificationException("Invalid code");
        }
    }
}
