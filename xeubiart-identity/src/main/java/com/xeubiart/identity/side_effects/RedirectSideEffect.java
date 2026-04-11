package com.xeubiart.identity.side_effects;

public record RedirectSideEffect(
    String url
) implements SideEffect {}
