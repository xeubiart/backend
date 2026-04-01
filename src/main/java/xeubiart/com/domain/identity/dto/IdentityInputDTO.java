package xeubiart.com.domain.identity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import xeubiart.com.domain.user.model.User;

@Data
@SuperBuilder
public abstract class IdentityInputDTO {
    @NotNull
    protected String email;

    protected User user;
}
