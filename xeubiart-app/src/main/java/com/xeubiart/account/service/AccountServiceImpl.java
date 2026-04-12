package com.xeubiart.account.service;

import com.xeubiart.account.entity.Account;
import com.xeubiart.account.exceptions.AccountInvalidCredentialsException;
import com.xeubiart.account.exceptions.AccountNotFoundException;
import com.xeubiart.account.mapper.AccountMapper;
import com.xeubiart.account.model.dto.AccountInputDTO;
import com.xeubiart.account.model.request.AccountLoginRequest;
import com.xeubiart.account.repository.AccountRepository;
import com.xeubiart.identity.exceptions.IdentityConflictException;
import com.xeubiart.identity.exceptions.IdentityInvalidCredentialsException;
import com.xeubiart.identity.exceptions.InvalidProviderException;
import com.xeubiart.identity.model.dto.IdentityInputDTO;
import com.xeubiart.identity.service.IdentityService;
import com.xeubiart.identity.side_effects.SetCookieSideEffect;
import com.xeubiart.identity.side_effects.SideEffect;
import com.xeubiart.verification.exceptions.BadVerificationException;
import com.xeubiart.verification.exceptions.VerificationAttemptsExceededException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService{
    private final IdentityService identityService;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Value("${server.servlet.session.cookie.name}")
    private String sessionCookieName;

    public AccountServiceImpl(IdentityService identityService, AccountRepository accountRepository, AccountMapper accountMapper) {
        this.identityService = identityService;
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    @Transactional
    public List<SideEffect> register(AccountInputDTO accountDTO, IdentityInputDTO identityDTO) throws InvalidProviderException{
        Account account = getOrCreateAccount(accountDTO);

        try {
            return this.identityService.register(account.getId(), identityDTO);
        } catch (IdentityConflictException ex) {
            try {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } catch (NoTransactionException ignored) {
                // Only happens during tests or non-transactional contexts
            }
            // Return "Fake" success to prevent account enumeration attacks
            return List.of(new SetCookieSideEffect(this.sessionCookieName, UUID.randomUUID().toString()));
        }
    }
    private Account getOrCreateAccount(AccountInputDTO dto) {
        return this.accountRepository.findByEmail(dto.email())
            .orElseGet(() -> {
                Account newAccount = this.accountMapper.toEntity(dto);
                try {
                    return this.accountRepository.saveAndFlush(newAccount);
                } catch (DataIntegrityViolationException ex) {
                    // Another thread won the race
                    return this.accountRepository.findByEmail(dto.email())
                        .orElseThrow(() -> new IllegalStateException(
                            "Account not found after conflict — this should never happen"
                        ));
                }
            });
    }

    @Override
    public List<SideEffect> login(AccountLoginRequest accountLoginRequest) throws IdentityInvalidCredentialsException {
        try {
            Account account = this.accountRepository.findByEmail(accountLoginRequest.email())
                .orElseThrow(IdentityInvalidCredentialsException::new);

            return this.identityService.login(account.getId(), accountLoginRequest.identityInputDTO());
        } catch (IdentityInvalidCredentialsException ex) {
            throw new AccountInvalidCredentialsException("Invalid credentials provided");
        }
    }

    @Override
    public List<SideEffect> verify(String token, String code) throws VerificationAttemptsExceededException, BadVerificationException {
        return this.identityService.verify(token, code);
    }

    @Override
    public void resendVerificationCode(String token) {
        this.identityService.newCode(token);
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return this.accountRepository.findById(id);
    }

    @Override
    public Optional<UUID> getAccountIdFromSession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return Optional.of(UUID.fromString(auth.getName()));
        }

        return Optional.empty();
    }
}
