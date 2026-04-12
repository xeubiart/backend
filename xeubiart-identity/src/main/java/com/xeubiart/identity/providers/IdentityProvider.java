package com.xeubiart.identity.providers;

import com.xeubiart.identity.exceptions.IdentityConflictException;
import com.xeubiart.identity.model.IdentityType;
import com.xeubiart.identity.model.dto.IdentityInputDTO;
import com.xeubiart.identity.providers.local.entity.IdentityLocal;
import com.xeubiart.identity.side_effects.SideEffect;

import java.util.List;
import java.util.UUID;

public interface IdentityProvider<T extends IdentityInputDTO> {
    List<SideEffect> createIdentity(UUID accountId, T identityDTO) throws IdentityConflictException;
    List<SideEffect> authenticateIdentity(UUID accountId, T identityDTO) throws IdentityConflictException;

    IdentityType getSupportedProvider();
    void markAsVerified(UUID accountId);
    IdentityLocal getIdentityLocal(UUID accountId);
}
