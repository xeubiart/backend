package com.xeubiart.verification.model.dto;

import com.xeubiart.identity.model.IdentityType;
import lombok.*;

import java.util.UUID;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class VerificationVerifyOutputDTO {
    IdentityType provider;
    UUID accountId;
}
