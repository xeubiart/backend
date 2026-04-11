package com.xeubiart.identity.service;

import com.xeubiart.identity.entity.Identity;
import com.xeubiart.identity.exceptions.IdentityConflictException;
import com.xeubiart.identity.exceptions.IdentityInvalidCredentialsException;
import com.xeubiart.identity.exceptions.InvalidProviderException;
import com.xeubiart.identity.model.IdentityType;
import com.xeubiart.identity.model.dto.IdentityInputDTO;
import com.xeubiart.identity.providers.IdentityProvider;
import com.xeubiart.identity.side_effects.SessionSideEffect;
import com.xeubiart.identity.side_effects.SetCookieSideEffect;
import com.xeubiart.identity.side_effects.SideEffect;
import com.xeubiart.identity.side_effects.RequireVerificationSideEffect;
import com.xeubiart.verification.entity.VerificationSession;
import com.xeubiart.verification.exceptions.BadVerificationException;
import com.xeubiart.verification.service.VerificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public boolean verify(String token, String code) {
        VerificationSession verification = this.verificationService.verify(token, code);
        if(verification != null){
            this.providers.get(verification.getProvider()).markAsVerified(verification.getAccountId());
            return true;
        }
        return false;
    }

    @Override
    public void newCode(String token) {
        this.verificationService.generateNew(token);
    }

    @Override
    public UUID getAccountIdFromSession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            String name = auth.getName();
            try {
                return UUID.fromString(name);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        return null;
    }

    // Transform sideEffects into webActions
    private SideEffect processSideEffects(SideEffect sideEffect, UUID accountId, IdentityType provider) {
        return switch (sideEffect) {
            case SessionSideEffect s -> s;
            case RequireVerificationSideEffect v -> {
                String cookie = this.verificationService.generate(accountId, provider);
                yield new SetCookieSideEffect("verify-token", cookie);
            }
            default -> sideEffect;
        };
    }
}
