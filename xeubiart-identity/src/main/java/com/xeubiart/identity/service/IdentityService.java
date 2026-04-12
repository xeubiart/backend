package com.xeubiart.identity.service;

import com.xeubiart.identity.exceptions.IdentityConflictException;
import com.xeubiart.identity.exceptions.IdentityInvalidCredentialsException;
import com.xeubiart.identity.exceptions.InvalidProviderException;
import com.xeubiart.identity.model.dto.IdentityInputDTO;
import com.xeubiart.identity.side_effects.SideEffect;
import com.xeubiart.verification.exceptions.BadVerificationException;
import com.xeubiart.verification.exceptions.VerificationAttemptsExceededException;

import java.util.List;
import java.util.UUID;

public interface IdentityService {
    List<SideEffect> register(UUID account_id, IdentityInputDTO identityDTO) throws InvalidProviderException, IdentityConflictException;
    List<SideEffect> login(UUID account_id, IdentityInputDTO identityDTO) throws IdentityInvalidCredentialsException;
    List<SideEffect> verify(String token, String code) throws VerificationAttemptsExceededException, BadVerificationException;
    void newCode(String token);
}
