package com.xeubiart.account.controller;

import com.xeubiart.account.model.request.AccountLoginRequest;
import com.xeubiart.account.model.request.AccountRegisterRequest;
import com.xeubiart.account.service.AccountService;
import com.xeubiart.identity.side_effects.SideEffect;
import com.xeubiart.infra.utils.SideEffectProcessor;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final SideEffectProcessor sideEffectProcessor;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody AccountRegisterRequest request, HttpSession session) {
        List<SideEffect> sideEffects = this.accountService.register(request.accountInputDTO(), request.identityInputDTO());
        return applyEffectsAndBuild(sideEffects, session);
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verify(@RequestParam(value = "code") String code, @CookieValue(value = "${server.servlet.verification.cookie.name}") String token, HttpSession session) {
        List<SideEffect> sideEffects = this.accountService.verify(token, code);
        return applyEffectsAndBuild(sideEffects, session);
    }

    @PostMapping("/new-code")
    public void newCode(@CookieValue(value = "${server.servlet.verification.cookie.name}") String token) {
        this.accountService.resendVerificationCode(token);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody AccountLoginRequest request, HttpSession session) {
        List<SideEffect> sideEffects = this.accountService.login(request);
        return applyEffectsAndBuild(sideEffects, session);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public String me() {
        return "Only authenticated users can access this endpoint";
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteAccounts() {
        return "Deleting all accounts";
    }

    private ResponseEntity<Void> applyEffectsAndBuild(List<SideEffect> sideEffects, HttpSession session) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        sideEffects.forEach(effect -> sideEffectProcessor.apply(effect, session, builder));
        return builder.build();
    }
}
