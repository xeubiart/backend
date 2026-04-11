package com.xeubiart.identity.side_effects;

public record SetCookieSideEffect(
    String name,
    String value
) implements SideEffect {}