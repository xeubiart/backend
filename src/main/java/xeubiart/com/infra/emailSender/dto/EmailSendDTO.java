package xeubiart.com.infra.emailSender.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailSendDTO {
    private String to;
    private String title;
    private String content;
}
