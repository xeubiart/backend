package com.xeubiart.identity.side_effects;

import org.springframework.security.core.userdetails.UserDetails;

public record SessionSideEffect(
    UserDetails principal
) implements SideEffect {}
