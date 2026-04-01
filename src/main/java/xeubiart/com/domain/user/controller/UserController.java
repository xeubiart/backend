package xeubiart.com.domain.user.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xeubiart.com.domain.identity.dto.IdentityOutputDTO;
import xeubiart.com.domain.identity.model.IdentityProviders;
import xeubiart.com.domain.identity.providers.local.dto.IdentityLocalInputDTO;
import xeubiart.com.domain.identity.providers.local.dto.IdentityLocalOutputDTO;
import xeubiart.com.domain.user.dto.LocalRegistrationRequest;
import xeubiart.com.domain.user.dto.UserInputDTO;
import xeubiart.com.domain.user.service.UserService;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/public/auth/register")
    public ResponseEntity<Void> register(@Valid @RequestBody LocalRegistrationRequest request){
        UserInputDTO userDTO = new UserInputDTO(request.getName());

        IdentityLocalInputDTO localRegisterDTO = IdentityLocalInputDTO.builder()
                .user(null) // Will be overrided in the registration process with the savedInstance of User created by userDTO
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        Optional<IdentityOutputDTO> registerResult = this.userService.register(userDTO, localRegisterDTO, IdentityProviders.LOCAL);

        String sessionToken = UUID.randomUUID().toString();
        if(registerResult.isPresent()){
            IdentityLocalOutputDTO result = (IdentityLocalOutputDTO) registerResult.get();

            sessionToken = result.getSessionToken().toString();
        }

        // Use the ID from the verification registry as sessionToken to identify the subsequential request
        ResponseCookie cookie = ResponseCookie.from("v-session", sessionToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .sameSite("Lax")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
