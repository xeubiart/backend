package com.xeubiart.identity.providers.local.service;

import com.xeubiart.identity.common.factory.FactoryIdentityTest;
import com.xeubiart.identity.exceptions.IdentityConflictException;
import com.xeubiart.identity.providers.local.entity.IdentityLocal;
import com.xeubiart.identity.providers.local.mapper.IdentityLocalMapper;
import com.xeubiart.identity.providers.local.model.dto.IdentityLocalInputDTO;
import com.xeubiart.identity.providers.local.repository.IdentityLocalRepository;
import com.xeubiart.identity.side_effects.SideEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IdentityLocalServiceTest {
    @Mock private IdentityLocalRepository identityLocalRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private IdentityLocalMapper identityLocalMapper;

    @InjectMocks
    private IdentityLocalServiceImpl identityLocalService;

    private UUID accountId;
    private IdentityLocalInputDTO identityDTO;

    @BeforeEach
    void setUp() {
        this.accountId = UUID.randomUUID();
        this.identityDTO = FactoryIdentityTest.localInputDTO();
    }

    @Test
    void createIdentity_ShouldSaveEntity(){
        IdentityLocal entity = new IdentityLocal();
        entity.setPassword(this.identityDTO.getPassword());

        when(this.identityLocalRepository.existsByAccountId(this.accountId)).thenReturn(false);
        when(this.identityLocalMapper.toEntity(identityDTO)).thenReturn(entity);
        when(this.passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        List<SideEffect> results = this.identityLocalService.createIdentity(this.accountId, this.identityDTO);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("encodedPassword", entity.getPassword());
        assertEquals(this.accountId, entity.getAccountId());

        verify(this.identityLocalRepository, times(1)).saveAndFlush(entity);
    }

    @Test
    void createIdentity_WhenAlreadyExists_ShouldThrowIdentityConflictException() {
        // Simulate that the account already has an identity
        when(identityLocalRepository.existsByAccountId(this.accountId)).thenReturn(true);

        assertThrows(IdentityConflictException.class, () -> {
            identityLocalService.createIdentity(this.accountId, this.identityDTO);
        });

        // Verify that we stopped early and never tried to save
        verify(identityLocalRepository, never()).saveAndFlush(any());
    }
}
