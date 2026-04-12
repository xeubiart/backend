package com.xeubiart.verification.service;

import com.xeubiart.identity.model.IdentityType;
import com.xeubiart.verification.entity.VerificationSession;
import com.xeubiart.verification.exceptions.BadVerificationException;
import com.xeubiart.verification.exceptions.VerificationAttemptsExceededException;
import com.xeubiart.verification.exceptions.VerificationReSendCooldownException;
import com.xeubiart.verification.model.dto.VerificationVerifyOutputDTO;

import java.util.UUID;

public interface VerificationService {
    String generate(UUID accountId, IdentityType provider);
    void generateNew(String sessionToken) throws BadVerificationException, VerificationReSendCooldownException;
    VerificationVerifyOutputDTO verify(String sessionToken, String code) throws BadVerificationException, VerificationAttemptsExceededException;
    void erase(String sessionToken);
}
