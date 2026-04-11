package com.xeubiart.identity.common.factory;

import com.xeubiart.identity.providers.local.model.dto.IdentityLocalInputDTO;

public final class FactoryIdentityTest {
    public static IdentityLocalInputDTO localInputDTO(){
        return IdentityLocalInputDTO.builder()
                .password("valid_password")
                .build();
    }
}
