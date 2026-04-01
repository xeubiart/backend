package xeubiart.com.domain.identity.providers;

import xeubiart.com.domain.identity.dto.IdentityInputDTO;
import xeubiart.com.domain.identity.dto.IdentityOutputDTO;
import xeubiart.com.domain.identity.model.Identity;
import xeubiart.com.domain.identity.model.IdentityProviders;
import xeubiart.com.domain.user.model.User;

public interface IdentityProvider {
    boolean supports(IdentityProviders type);

    IdentityOutputDTO createIdentity(User user, IdentityInputDTO dto);
}