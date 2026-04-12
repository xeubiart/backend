package com.xeubiart.identity.service;

import com.xeubiart.identity.entity.Identity;
import com.xeubiart.identity.exceptions.IdentityConflictException;
import com.xeubiart.identity.exceptions.IdentityInvalidCredentialsException;
import com.xeubiart.identity.exceptions.InvalidProviderException;
import com.xeubiart.identity.model.IdentityType;
import com.xeubiart.identity.model.dto.IdentityInputDTO;
import com.xeubiart.identity.model.dto.IdentityPrincipal;
import com.xeubiart.identity.providers.IdentityProvider;
import com.xeubiart.identity.side_effects.*;
import com.xeubiart.verification.exceptions.BadVerificationException;
import com.xeubiart.verification.exceptions.VerificationAttemptsExceededException;
import com.xeubiart.verification.model.dto.VerificationVerifyOutputDTO;
import com.xeubiart.verification.service.VerificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IdentityServiceImpl implements IdentityService {
    private final Map<IdentityType, IdentityProvider<?>> providers;
    private final VerificationService verificationService;

    @Value("${server.servlet.verification.cookie.name}")
    private String verificationCookieName;

    @SuppressWarnings("unchecked")
    public IdentityServiceImpl(List<IdentityProvider<?>> providers, VerificationService verificationService) {
        this.providers = providers.stream()
            .collect(Collectors.toMap(
                IdentityProvider::getSupportedProvider,
                p -> p
            ));

        this.verificationService = verificationService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SideEffect> register(UUID accountId, IdentityInputDTO identityDTO) throws InvalidProviderException, IdentityConflictException {
        IdentityType identityType = identityDTO.getProvider();
        IdentityProvider provider = providers.get(identityType);

        if (provider == null) {
            throw new InvalidProviderException(identityType.name());
        }

        List<SideEffect> effects = provider.createIdentity(accountId, identityDTO);

        return effects.stream()
                .map(sideEffect -> this.processSideEffects(sideEffect, accountId, identityDTO.getProvider()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SideEffect> login(UUID accountId, IdentityInputDTO identityDTO) throws IdentityInvalidCredentialsException {
        IdentityType identityType = identityDTO.getProvider();
        IdentityProvider provider = providers.get(identityType);

        if (provider == null) {
            throw new InvalidProviderException(identityType.name());
        }

        List<SideEffect> effects = provider.authenticateIdentity(accountId, identityDTO);

        return effects.stream()
                .map(sideEffect -> this.processSideEffects(sideEffect, accountId, identityDTO.getProvider()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<SideEffect> verify(String token, String code) throws BadVerificationException, VerificationAttemptsExceededException {
        VerificationVerifyOutputDTO verificationOutput = this.verificationService.verify(token, code);

        IdentityProvider<?> provider = this.providers.get(verificationOutput.getProvider());

        provider.markAsVerified(verificationOutput.getAccountId());
        Identity identity = provider.getIdentityLocal(verificationOutput.getAccountId());

        IdentityPrincipal identityPrincipal = IdentityPrincipal.builder()
                .accountId(identity.getAccountId())
                // .authorities() should be set in the SessionSideEffect resolver
                .build();

        return List.of(new DeleteCookieSideEffect(this.verificationCookieName), new SessionSideEffect(identityPrincipal));
    }

    @Override
    public void newCode(String token) {
        this.verificationService.generateNew(token);
    }

    // Transform sideEffects into webActions
    private SideEffect processSideEffects(SideEffect sideEffect, UUID accountId, IdentityType provider) {
        return switch (sideEffect) {
            case SessionSideEffect s -> s;
            case RequireVerificationSideEffect v -> {
                String cookie = this.verificationService.generate(accountId, provider);
                yield new SetCookieSideEffect(this.verificationCookieName, cookie);
            }
            default -> sideEffect;
        };
    }
}
