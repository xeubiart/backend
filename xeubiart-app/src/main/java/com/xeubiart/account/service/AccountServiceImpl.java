package com.xeubiart.account.service;

import com.xeubiart.account.entity.Account;
import com.xeubiart.account.exceptions.AccountInvalidCredentialsException;
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
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService{
    private final IdentityService identityService;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public List<SideEffect> register(AccountInputDTO accountDTO, IdentityInputDTO identityDTO) throws InvalidProviderException{
        Account account = this.accountRepository.findByEmail(accountDTO.email())
            .orElseGet(() -> {
                Account Account = this.accountMapper.toEntity(accountDTO);
                return this.accountRepository.saveAndFlush(Account);
            });

        List<SideEffect> sideEffects;
        try {
            sideEffects = this.identityService.register(account.getId(), identityDTO);
        } catch (IdentityConflictException ex){
            // Mark the transaction to roll back
            try {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } catch (NoTransactionException ignored) {
                // This will happen within @Test, so we can safely ignore it
            }
            // Pretend the register was successful and notify the user that someone tried to register with her email
            // Override the side effects with a fake one
            return List.of(new SetCookieSideEffect("v-session", UUID.randomUUID().toString()));
        }

        return sideEffects;
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
    public boolean verify(String token, String code) {
        return this.identityService.verify(token, code);
    }

    @Override
    public void newCode(String token) {
        this.identityService.newCode(token);
    }
}
