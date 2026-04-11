package com.xeubiart.account.service;

import com.xeubiart.account.model.dto.AccountInputDTO;
import com.xeubiart.account.model.request.AccountLoginRequest;
import com.xeubiart.identity.exceptions.IdentityInvalidCredentialsException;
import com.xeubiart.identity.exceptions.InvalidProviderException;
import com.xeubiart.identity.model.dto.IdentityInputDTO;
import com.xeubiart.identity.side_effects.SideEffect;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface AccountService {
    List<SideEffect> register(AccountInputDTO accountDTO, IdentityInputDTO identityDTO) throws InvalidProviderException;
    List<SideEffect> login(AccountLoginRequest accountLoginRequest) throws IdentityInvalidCredentialsException;
    boolean verify(String token, String code);
    void newCode(String token);
}
