package xeubiart.com.domain.identity.providers.local.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class VerificationLocalOutputDTO {
    private UUID id;
}
