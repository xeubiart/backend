package xeubiart.com.domain.identity.providers.local.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xeubiart.com.domain.identity.providers.local.service.VerificationLocalService;

import java.time.Duration;

@RestController
@RequestMapping("/api/public/auth")
@AllArgsConstructor
public class VerificationController {
    private final VerificationLocalService service;

    @PostMapping("/verify")
    public ResponseEntity<String> verify(
        @CookieValue(name = "v-session", required = true) String sessionToken,
        @RequestParam String code
    ){
        this.service.verify(sessionToken, code);

        // Clear the cookie by returning an expired one
        ResponseCookie deleteCookie = ResponseCookie.from("v-session", "")
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();
    }

    @PostMapping("/verify/resend")
    public ResponseEntity<String> resend(
            @CookieValue(name = "v-session", required = true) String sessionToken
    ){
        this.service.generateNewCode(sessionToken);

        // TODO: maybe be a better idea to update the sessionToken
        // Reset the session duration
        ResponseCookie cookie = ResponseCookie.from("v-session", sessionToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
