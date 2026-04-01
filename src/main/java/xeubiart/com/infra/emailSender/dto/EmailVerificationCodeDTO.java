package xeubiart.com.infra.emailSender.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailVerificationCodeDTO {
    String code;
    String email;
    String name;
}
