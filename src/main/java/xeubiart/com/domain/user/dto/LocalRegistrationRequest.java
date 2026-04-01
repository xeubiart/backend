package xeubiart.com.domain.user.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import xeubiart.com.domain.identity.providers.local.dto.IdentityLocalInputDTO;

@Data
@AllArgsConstructor
public class LocalRegistrationRequest {
    // Same fields from UserInputDTO
    @NotNull
    private String name;

    // Same fields from IdentityLocalInputDTO
    @NotNull
    private String email;
    @NotNull
    private String password;
}
