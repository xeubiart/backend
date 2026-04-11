package com.xeubiart.identity.providers.local.model.dto;

import com.xeubiart.identity.model.IdentityType;
import com.xeubiart.identity.model.dto.IdentityInputDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @SuperBuilder
public class IdentityLocalInputDTO extends IdentityInputDTO {
    @NotBlank
    private String password;

    @Override
    public IdentityType getProvider() {
        return IdentityType.LOCAL;
    }
}
