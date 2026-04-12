package com.xeubiart.identity.side_effects;

import com.xeubiart.identity.model.dto.IdentityPrincipal;

public record SessionSideEffect(
    IdentityPrincipal principal
) implements SideEffect {}
