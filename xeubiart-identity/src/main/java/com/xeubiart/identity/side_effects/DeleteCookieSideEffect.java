package com.xeubiart.identity.side_effects;

public record DeleteCookieSideEffect(
    String name
) implements SideEffect {}
