package com.xeubiart.account.service;

import com.xeubiart.account.entity.Account;
import com.xeubiart.account.mapper.AccountMapper;
import com.xeubiart.account.model.dto.AccountInputDTO;
import com.xeubiart.account.repository.AccountRepository;
import com.xeubiart.identity.exceptions.IdentityConflictException;
import com.xeubiart.identity.model.dto.IdentityInputDTO;
import com.xeubiart.identity.providers.local.model.dto.IdentityLocalInputDTO;
import com.xeubiart.identity.service.IdentityService;
import com.xeubiart.identity.side_effects.SetCookieSideEffect;
import com.xeubiart.identity.side_effects.SideEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock private IdentityService identityService;
    @Mock private AccountRepository accountRepository;
    @Mock private AccountMapper accountMapper;

    @InjectMocks private AccountServiceImpl accountService;

    private IdentityInputDTO identityDTO;
    private AccountInputDTO accountDTO;

    @BeforeEach
    void setUp() {
        this.identityDTO = IdentityLocalInputDTO.builder()
                .password("valid_password")
                .build();

        this.accountDTO = new AccountInputDTO("valid@email.com");
    }

    @Test
    void register_ShouldSaveAccount(){
        Account account = new Account();
        account.setEmail(this.accountDTO.email());
        account.setId(UUID.randomUUID());

        when(this.accountRepository.findByEmail(this.accountDTO.email())).thenReturn(Optional.empty());
        when(this.accountMapper.toEntity(this.accountDTO)).thenReturn(account);
        when(this.accountRepository.saveAndFlush(account)).thenReturn(account);

        List<SideEffect> expectedSideEffects = List.of(new SetCookieSideEffect("v-session", "random_value"));

        when(this.identityService.register(account.getId(), this.identityDTO)).thenReturn(expectedSideEffects);

        List<SideEffect> result = this.accountService.register(this.accountDTO, this.identityDTO);

        assertEquals(expectedSideEffects, result);
        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isInstanceOf(SetCookieSideEffect.class);

        verify(this.accountRepository).saveAndFlush(any(Account.class));
        verify(this.identityService).register(any(UUID.class), any(IdentityInputDTO.class));
        verify(this.accountMapper).toEntity(any(AccountInputDTO.class));
        verify(this.accountRepository).findByEmail(any(String.class));
        verify(this.accountRepository).saveAndFlush(any(Account.class));
    }

    @Test
    void register_WhenIdentityConflicts_ShouldReturnFakeSuccess() throws Exception {
        // ARRANGE
        Account existingAccount = new Account();
        existingAccount.setId(UUID.randomUUID());

        // Simulates account ALREADY exists
        when(this.accountRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(existingAccount));

        // Simulate the identity conflict
        when(this.identityService.register(any(), any()))
                .thenThrow(new IdentityConflictException("Conflict!"));

        // ACT
        List<SideEffect> result = this.accountService.register(this.accountDTO, this.identityDTO);

        // ASSERT
        assertEquals(1, result.size());
        assertInstanceOf(SetCookieSideEffect.class, result.getFirst());
        // Verify the "fake" cookie is returned even though a conflict happened
        assertEquals("v-session", ((SetCookieSideEffect)result.getFirst()).name());
    }

}
