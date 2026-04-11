package com.xeubiart.account.controller;

import com.xeubiart.account.model.request.AccountLoginRequest;
import com.xeubiart.account.model.request.AccountRegisterRequest;
import com.xeubiart.account.service.AccountService;
import com.xeubiart.identity.side_effects.DeleteCookieSideEffect;
import com.xeubiart.identity.side_effects.SideEffect;
import com.xeubiart.infra.utils.SideEffectProcessor;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/account")
public class AccountController {
    private final AccountService accountService;
    private final SideEffectProcessor sideEffectProcessor;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody AccountRegisterRequest request, HttpSession session) {
        List<SideEffect> sideEffects = this.accountService.register(request.accountInputDTO(), request.identityInputDTO());
        return buildResponse(sideEffects, session);
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verify(@RequestParam(value = "code") String code, @CookieValue(value = "verify-token") String token, HttpSession session) {
        if(this.accountService.verify(token, code)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/new-code")
    public void newCode(@CookieValue(value = "verify-token") String token, HttpSession session) {
        this.accountService.newCode(token);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody AccountLoginRequest request, HttpSession session) {
        List<SideEffect> sideEffects = this.accountService.login(request);
        return buildResponse(sideEffects, session);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public void me() {
        System.out.println("Only authenticated users can access this endpoint");
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return buildResponse(List.of(new DeleteCookieSideEffect("v-session")), session);
    }

    private ResponseEntity<Void> buildResponse(List<SideEffect> sideEffects, HttpSession session) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        sideEffects.forEach(effect -> sideEffectProcessor.apply(effect, session, builder));
        return builder.build();
    }
}
