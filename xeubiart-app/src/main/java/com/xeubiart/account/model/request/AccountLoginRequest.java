package com.xeubiart.account.model.request;

import com.xeubiart.identity.model.dto.IdentityInputDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountLoginRequest(
    @NotBlank
    @Email
    String email,
    @NotNull
    IdentityInputDTO identityInputDTO
){}
