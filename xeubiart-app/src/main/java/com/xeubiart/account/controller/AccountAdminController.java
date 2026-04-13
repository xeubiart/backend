package com.xeubiart.account.controller;

import com.xeubiart.account.exceptions.AccountInvalidCredentialsException;
import com.xeubiart.account.model.AccountRole;
import com.xeubiart.account.model.request.AccountLoginRequest;
import com.xeubiart.account.service.AccountService;
import com.xeubiart.identity.side_effects.SideEffect;
import com.xeubiart.infra.utils.SideEffectProcessor;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/account/admin")
public class AccountAdminController {
    private final AccountService accountService;
    private final SideEffectProcessor sideEffectProcessor;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody AccountLoginRequest request, HttpSession session){
        this.accountService.findByEmailAndRole(request.email(), AccountRole.ADMIN)
                .orElseThrow(() -> new AccountInvalidCredentialsException("Invalid credentials provided"));

        List< SideEffect> sideEffects = this.accountService.login(request);
        return applyEffectsAndBuild(sideEffects, session);
    }

    private ResponseEntity<Void> applyEffectsAndBuild(List<SideEffect> sideEffects, HttpSession session) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        sideEffects.forEach(effect -> sideEffectProcessor.apply(effect, session, builder));
        return builder.build();
    }
}
