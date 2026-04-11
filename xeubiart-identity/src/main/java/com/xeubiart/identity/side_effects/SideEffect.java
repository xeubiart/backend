package com.xeubiart.identity.side_effects;

public sealed interface SideEffect permits
    SetCookieSideEffect,
    RedirectSideEffect,
    SessionSideEffect,
    RequireVerificationSideEffect,
    DeleteCookieSideEffect
{}
