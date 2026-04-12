package com.xeubiart.infra.utils;

import com.xeubiart.account.entity.Account;
import com.xeubiart.account.service.AccountService;
import com.xeubiart.identity.model.dto.IdentityPrincipal;
import com.xeubiart.identity.side_effects.*;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@AllArgsConstructor
public class SideEffectProcessor {
    private final AccountService accountService;

    public void apply(SideEffect effect, HttpSession session, ResponseEntity.BodyBuilder response) {
        switch (effect) {
            case SetCookieSideEffect cookie ->
                    response.header(HttpHeaders.SET_COOKIE, buildCookie(cookie));

            case SessionSideEffect(IdentityPrincipal principal) -> {
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                Account account = this.accountService.findById(principal.getAccountId())
                        .orElseThrow(() -> new IllegalStateException("Account not found"));

                context.setAuthentication(new UsernamePasswordAuthenticationToken(
                        principal, null, List.of(new SimpleGrantedAuthority("ROLE_" + account.getRole().name()))
                ));
                session.setAttribute("SPRING_SECURITY_CONTEXT", context);
            }

            case RedirectSideEffect redirect ->
                    response.header(HttpHeaders.LOCATION, redirect.url());

            case DeleteCookieSideEffect deleteCookie ->
                    response.header(HttpHeaders.SET_COOKIE, this.buildDeleteCookie(deleteCookie));

            case RequireVerificationSideEffect v ->
                // shouldn't reach here after IdentityServiceImpl transforms it,
                // but handle defensively
                throw new IllegalStateException("RequireVerificationSideEffect should have been resolved");
            default -> throw new IllegalStateException("Unexpected value: " + effect);
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

    private String buildDeleteCookie(DeleteCookieSideEffect cookie) {
        return ResponseCookie.from(cookie.name(), "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .maxAge(0)
                .path("/")
                .build()
                .toString();
    }
}
