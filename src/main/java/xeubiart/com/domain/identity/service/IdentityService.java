package xeubiart.com.domain.identity.service;

import xeubiart.com.domain.identity.dto.IdentityInputDTO;
import xeubiart.com.domain.identity.dto.IdentityOutputDTO;
import xeubiart.com.domain.identity.model.IdentityProviders;
import xeubiart.com.domain.user.model.User;

import java.util.Optional;

public interface IdentityService {
    IdentityOutputDTO setupIdentity(User user, IdentityInputDTO dto, IdentityProviders provider);

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
