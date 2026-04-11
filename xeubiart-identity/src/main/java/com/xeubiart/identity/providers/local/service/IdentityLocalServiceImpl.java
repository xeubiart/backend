package com.xeubiart.identity.providers.local.service;

import com.xeubiart.identity.exceptions.IdentityConflictException;
import com.xeubiart.identity.exceptions.IdentityInvalidCredentialsException;
import com.xeubiart.identity.model.IdentityType;
import com.xeubiart.identity.providers.IdentityProvider;
import com.xeubiart.identity.providers.local.entity.IdentityLocal;
import com.xeubiart.identity.providers.local.mapper.IdentityLocalMapper;
import com.xeubiart.identity.providers.local.model.dto.IdentityLocalInputDTO;
import com.xeubiart.identity.providers.local.repository.IdentityLocalRepository;
import com.xeubiart.identity.side_effects.SessionSideEffect;
import com.xeubiart.identity.side_effects.SideEffect;
import com.xeubiart.identity.side_effects.RequireVerificationSideEffect;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IdentityLocalServiceImpl implements IdentityProvider<IdentityLocalInputDTO>, UserDetailsService {
    private final IdentityLocalRepository identityLocalRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdentityLocalMapper identityLocalMapper;

    @Override
    @Transactional
    public List<SideEffect> createIdentity(UUID accountId, IdentityLocalInputDTO identityDTO) throws IdentityConflictException {
        if (identityLocalRepository.existsByAccountId(accountId)) {
            throw new IdentityConflictException("Account already has a local identity");
        }

        IdentityLocal entity = this.identityLocalMapper.toEntity(identityDTO);
        entity.setAccountId(accountId);
        entity.setPassword(this.passwordEncoder.encode(entity.getPassword()));
        entity.setActive(false);
        entity.setProvider(IdentityType.LOCAL);

        try {
            this.identityLocalRepository.saveAndFlush(entity);
        } catch (DataIntegrityViolationException e) {
            // This catches the case where two threads passed the 'if' at the same time
            throw new IdentityConflictException("Account already has a local identity");
        }

        return List.of(new RequireVerificationSideEffect());
    }

    @Override
    public List<SideEffect> authenticateIdentity(UUID accountId, IdentityLocalInputDTO identityDTO) throws IdentityInvalidCredentialsException {
        IdentityLocal identityLocal = identityLocalRepository.findByAccountId(accountId)
                .orElseThrow(IdentityInvalidCredentialsException::new);

        if(!identityLocal.isActive()) throw new IdentityInvalidCredentialsException();

        if(!this.passwordEncoder.matches(identityDTO.getPassword(), identityLocal.getPassword())){
            throw new IdentityInvalidCredentialsException();
        }

        // Change it from the whole LocalIdentity, to a DTO or something
        return List.of(new SessionSideEffect(identityLocal));
    }

    @Override
    public IdentityType getSupportedProvider() {
        return IdentityType.LOCAL;
    }

    @Override
    @Transactional
    public void markAsVerified(UUID accountId) {
        IdentityLocal identityLocal = identityLocalRepository.findByAccountId(accountId)
                .orElseThrow(IdentityInvalidCredentialsException::new);

        identityLocal.setActive(true);
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return this.identityLocalRepository.findByAccountId(UUID.fromString(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
