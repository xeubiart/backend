package xeubiart.com.domain.identity.providers.local.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import xeubiart.com.domain.identity.dto.IdentityOutputDTO;

import java.util.UUID;

@Setter
@Getter
public class IdentityLocalOutputDTO extends IdentityOutputDTO {
    private UUID sessionToken;
}
