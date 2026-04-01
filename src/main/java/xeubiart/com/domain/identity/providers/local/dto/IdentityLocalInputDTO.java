package xeubiart.com.domain.identity.providers.local.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import xeubiart.com.domain.identity.dto.IdentityInputDTO;

@SuperBuilder
@Getter
public class IdentityLocalInputDTO extends IdentityInputDTO {
    @NotNull
    private String password;
}
