package com.xeubiart.account.model.dto;

import jakarta.validation.constraints.Email;

public record AccountInputDTO(
    @Email
    String email
){}
