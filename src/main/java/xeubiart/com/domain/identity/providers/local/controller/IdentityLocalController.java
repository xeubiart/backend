package xeubiart.com.domain.identity.providers.local.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import xeubiart.com.domain.identity.providers.local.dto.LoginRequestDTO;

@RestController
@RequestMapping("/api/public/auth")
@AllArgsConstructor
public class IdentityLocalController {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDTO dto, HttpServletRequest request, HttpServletResponse response){
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
                dto.getEmail(),   // Spring treats it internally as username, even though is the email...
                dto.getPassword()
        );

        // Authenticate using LocalCredentialsService
        Authentication authResult = authenticationManager.authenticate(token);

        // Save in the Local thread contextHolder
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);

        // Persists in DB and cookie
        this.securityContextRepository.saveContext(context, request, response);

        return ResponseEntity.ok()
                .build();
    }
}
