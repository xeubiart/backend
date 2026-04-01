package xeubiart.com.domain.identity.providers.local.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class LoginRequestDTO {
    @Email
    @NotBlank
    String email;
    @NotBlank
    @Size(min = 8)
    String password;
}
