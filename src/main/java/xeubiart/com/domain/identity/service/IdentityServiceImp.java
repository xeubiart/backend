package xeubiart.com.domain.identity.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xeubiart.com.domain.identity.dto.IdentityInputDTO;
import xeubiart.com.domain.identity.dto.IdentityOutputDTO;
import xeubiart.com.domain.identity.model.Identity;
import xeubiart.com.domain.identity.model.IdentityProviders;
import xeubiart.com.domain.identity.providers.IdentityProvider;
import xeubiart.com.domain.identity.providers.local.dto.IdentityLocalOutputDTO;
import xeubiart.com.domain.identity.repository.IdentityRepository;
import xeubiart.com.domain.user.model.User;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class IdentityServiceImp implements IdentityService, UserDetailsService {
    private final List<IdentityProvider> providers;
    private final IdentityRepository repository;

    @Override
    @Transactional
    public IdentityOutputDTO setupIdentity(User user, IdentityInputDTO dto, IdentityProviders provider) {
        // 1. Find the specialized strategy (Local, Google, etc.)
        IdentityProvider prov = this.providers.stream()
                .filter(p -> p.supports(provider))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Provider not supported"));

        // 2. The strategy creates the SPECIFIC subclass
        IdentityOutputDTO identityOutput = prov.createIdentity(user, dto);

        // 3. Save once.
        // Hibernate inserts into Parent table AND Child table automatically.
        this.repository.save(identityOutput.getIdentity());

        return identityOutput;
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.repository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return this.repository.findUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // We find the Identity by email.
        // Because of JOINED inheritance, Hibernate will return the specific
        // subclass (IdentityLocal, etc.) automatically.
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
