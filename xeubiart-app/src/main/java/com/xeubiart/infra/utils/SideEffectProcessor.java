package com.xeubiart.infra.utils;

import com.xeubiart.identity.side_effects.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class SideEffectProcessor {
    public void apply(SideEffect effect, HttpSession session, ResponseEntity.BodyBuilder response) {
        switch (effect) {
            case SetCookieSideEffect cookie ->
                    response.header(HttpHeaders.SET_COOKIE, buildCookie(cookie));

            case SessionSideEffect(UserDetails principal) -> {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(new UsernamePasswordAuthenticationToken(
                        principal, null, principal.getAuthorities()
                ));
                session.setAttribute("SPRING_SECURITY_CONTEXT", context);
            }

            case RedirectSideEffect redirect ->
                    response.header(HttpHeaders.LOCATION, redirect.url());

            case RequireVerificationSideEffect v ->
                // shouldn't reach here after IdentityServiceImpl transforms it,
                // but handle defensively
                throw new IllegalStateException("RequireVerificationSideEffect should have been resolved");
        }
    }

    private String buildCookie(SetCookieSideEffect cookie) {
        return ResponseCookie.from(cookie.name(), cookie.value())
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .maxAge(Duration.ofMinutes(15))
                .path("/")
                .build()
                .toString();
    }
}
