package com.xeubiart.account.model.request;

import com.xeubiart.account.model.dto.AccountInputDTO;
import com.xeubiart.identity.model.dto.IdentityInputDTO;
import jakarta.validation.constraints.NotNull;

public record AccountRegisterRequest(
    @NotNull
    AccountInputDTO accountInputDTO,
    @NotNull
    IdentityInputDTO identityInputDTO
){}
