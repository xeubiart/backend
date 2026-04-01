package xeubiart.com.domain.identity.providers.local.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xeubiart.com.domain.identity.dto.IdentityInputDTO;
import xeubiart.com.domain.identity.dto.IdentityOutputDTO;
import xeubiart.com.domain.identity.model.Identity;
import xeubiart.com.domain.identity.model.IdentityProviders;
import xeubiart.com.domain.identity.providers.IdentityProvider;
import xeubiart.com.domain.identity.providers.local.dto.IdentityLocalInputDTO;
import xeubiart.com.domain.identity.providers.local.dto.IdentityLocalOutputDTO;
import xeubiart.com.domain.identity.providers.local.dto.VerificationLocalOutputDTO;
import xeubiart.com.domain.identity.providers.local.entity.IdentityLocal;
import xeubiart.com.domain.identity.providers.local.mapper.IdentityLocalMapper;
import xeubiart.com.domain.identity.providers.local.repository.IdentityLocalRepository;
import xeubiart.com.domain.user.model.User;

import java.util.UUID;

@Service
@AllArgsConstructor
public class IdentityLocalService implements IdentityProvider {
    private final VerificationLocalService verificationService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public IdentityOutputDTO createIdentity(User user, IdentityInputDTO dto){
        IdentityLocalInputDTO localDto = (IdentityLocalInputDTO) dto;

        // Build the subclass
        IdentityLocal local = new IdentityLocal();
        local.setUser(user);
        local.setEmail(dto.getEmail());
        local.setPassword(passwordEncoder.encode(localDto.getPassword()));
        local.setActive(false);

        UUID sessionToken = this.verificationService.createVerification(local);

        IdentityLocalOutputDTO result = new IdentityLocalOutputDTO();
        result.setIdentity(local);
        result.setSessionToken(sessionToken);

        return result;
    }

    @Override
    public boolean supports(IdentityProviders provider) {
        return provider == IdentityProviders.LOCAL;
    }
}
