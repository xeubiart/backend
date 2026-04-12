package com.xeubiart.account.service;

import com.xeubiart.account.entity.Account;
import com.xeubiart.account.model.dto.AccountInputDTO;
import com.xeubiart.account.model.request.AccountLoginRequest;
import com.xeubiart.identity.exceptions.IdentityInvalidCredentialsException;
import com.xeubiart.identity.exceptions.InvalidProviderException;
import com.xeubiart.identity.model.dto.IdentityInputDTO;
import com.xeubiart.identity.side_effects.SideEffect;
import com.xeubiart.verification.exceptions.BadVerificationException;
import com.xeubiart.verification.exceptions.VerificationAttemptsExceededException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountService {
    List<SideEffect> register(AccountInputDTO accountDTO, IdentityInputDTO identityDTO) throws InvalidProviderException;
    List<SideEffect> login(AccountLoginRequest accountLoginRequest) throws IdentityInvalidCredentialsException;
    List<SideEffect> verify(String token, String code) throws VerificationAttemptsExceededException, BadVerificationException;
    void resendVerificationCode(String token);

    Optional<UUID> getAccountIdFromSession();
    Optional<Account> findById(UUID id);
}
