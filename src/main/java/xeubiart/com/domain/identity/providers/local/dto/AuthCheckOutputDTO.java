package xeubiart.com.domain.identity.providers.local.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
public class AuthCheckOutputDTO {
    String username;
}
