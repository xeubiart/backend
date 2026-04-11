package com.xeubiart.identity.model.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.xeubiart.identity.model.IdentityType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "provider")
@NoArgsConstructor @SuperBuilder
public abstract class IdentityInputDTO {
    public abstract IdentityType getProvider();
}
