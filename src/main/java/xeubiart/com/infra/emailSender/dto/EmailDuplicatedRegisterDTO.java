package xeubiart.com.infra.emailSender.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailDuplicatedRegisterDTO {
    String email;
    String name;
}
